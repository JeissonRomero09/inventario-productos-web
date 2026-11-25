const state = { products: [], dashboard: null, movements: [] };
const currency = new Intl.NumberFormat("es-CO", { style: "currency", currency: "COP", maximumFractionDigits: 0 });
const number = new Intl.NumberFormat("es-CO");
const $ = (selector, root = document) => root.querySelector(selector);
const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];
const escapeHtml = value => String(value ?? "").replace(/[&<>"']/g, char => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[char]);
const dateTime = value => value ? new Intl.DateTimeFormat("es-CO", { dateStyle: "medium", timeStyle: "short" }).format(new Date(value)) : "Fecha no disponible";
const productMark = name => escapeHtml((name || "?").trim().slice(0, 2));
const tones = ["", "green-tone", "blue-tone", "orange-tone"];

async function api(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: { "Content-Type": "application/json", ...(options.headers || {}) }
    });
    if (!response.ok) {
        let message = `No se pudo completar la solicitud (${response.status}).`;
        try {
            const body = await response.json();
            if (body.message) message = body.message;
        } catch { /* The response may not contain JSON. */ }
        throw new Error(message);
    }
    return response.status === 204 ? null : response.json();
}

function showNotice(message, kind = "error") {
    const element = $(`#${kind}-banner`);
    element.textContent = message;
    element.classList.remove("hidden");
    window.setTimeout(() => element.classList.add("hidden"), 5000);
}

function productStatus(product) {
    return product.stock <= product.lowStockThreshold
        ? '<span class="badge low">Stock bajo</span>'
        : '<span class="badge ok">Disponible</span>';
}

function productRow(product, actions = false) {
    const tone = tones[product.id % tones.length];
    return `<tr>
        <td><div class="item-cell"><span class="product-avatar ${tone}">${productMark(product.name)}</span><div class="item-name"><strong>${escapeHtml(product.name)}</strong><span>${escapeHtml(product.sku)}</span></div></div></td>
        <td><span class="category-label">${escapeHtml(product.category)}</span></td>
        <td class="money">${currency.format(product.unitPrice)}</td>
        <td><span class="stock-number ${product.stock <= product.lowStockThreshold ? "low" : ""}">${number.format(product.stock)}</span> <span class="muted">unid.</span></td>
        <td class="money">${currency.format(product.stockValue)}</td>
        <td>${productStatus(product)}</td>
        ${actions ? `<td><div class="row-actions">
            <button class="action-button" data-action="history" data-id="${product.id}" title="Ver historial" aria-label="Ver historial">↺</button>
            <button class="action-button" data-action="entry" data-id="${product.id}" title="Registrar entrada" aria-label="Registrar entrada">＋</button>
            <button class="action-button" data-action="exit" data-id="${product.id}" title="Registrar salida" aria-label="Registrar salida">−</button>
            <button class="action-button" data-action="edit" data-id="${product.id}" title="Editar artículo" aria-label="Editar artículo">✎</button>
            <button class="action-button delete" data-action="delete" data-id="${product.id}" title="Eliminar artículo" aria-label="Eliminar artículo">×</button>
        </div></td>` : ""}
    </tr>`;
}

function movementRow(movement) {
    const isEntry = movement.type === "ENTRY";
    return `<tr>
        <td>${escapeHtml(dateTime(movement.createdAt))}</td>
        <td><div class="item-name"><strong>${escapeHtml(movement.productName)}</strong><span>${escapeHtml(movement.productSku)}</span></div></td>
        <td><span class="badge ${isEntry ? "entry" : "exit"}">${isEntry ? "Entrada" : "Salida"}</span></td>
        <td>${isEntry ? "+" : "−"}${number.format(movement.quantity)} unid.</td>
        <td class="money">${currency.format(movement.unitPriceSnapshot)}</td>
        <td class="money">${currency.format(movement.totalValue)}</td>
        <td>${escapeHtml(movement.note || "—")}</td>
    </tr>`;
}

function renderDashboard() {
    const dashboard = state.dashboard;
    $("#metric-value").textContent = currency.format(dashboard.stockValue);
    $("#metric-products").textContent = number.format(dashboard.productCount);
    $("#metric-units").textContent = number.format(dashboard.totalUnits);
    $("#metric-low").textContent = number.format(dashboard.lowStockCount);
    const lowList = $("#low-stock-list");
    lowList.innerHTML = dashboard.lowStockProducts.length
        ? dashboard.lowStockProducts.slice(0, 5).map((product, index) => `<div class="low-stock-item">
            <span class="product-avatar ${tones[product.id % tones.length]}">${productMark(product.name)}</span>
            <div class="low-stock-copy"><strong>${escapeHtml(product.name)}</strong><span>${escapeHtml(product.sku)} · ${escapeHtml(product.category)}</span></div>
            <span class="stock-alert">${number.format(product.stock)} / ${number.format(product.lowStockThreshold)} unid.</span>
        </div>`).join("")
        : '<div class="empty-state"><span class="empty-symbol">✓</span>Todo en orden. No hay artículos bajo el umbral mínimo.</div>';
    $("#recent-list").innerHTML = dashboard.recentMovements.length
        ? dashboard.recentMovements.slice(0, 5).map(movement => `<div class="activity-item">
            <span class="movement-mark ${movement.type === "ENTRY" ? "entry" : "exit"}">${movement.type === "ENTRY" ? "↙" : "↗"}</span>
            <div class="activity-copy"><strong>${escapeHtml(movement.productName)}</strong><span>${movement.type === "ENTRY" ? "Entrada" : "Salida"} · ${escapeHtml(dateTime(movement.createdAt))}</span></div>
            <span class="activity-quantity">${movement.type === "ENTRY" ? "+" : "−"}${number.format(movement.quantity)}</span>
        </div>`).join("")
        : '<div class="empty-state">Aún no hay movimientos registrados.</div>';
    const recentProducts = [...state.products].sort((a, b) => new Date(b.updatedAt || b.createdAt || 0) - new Date(a.updatedAt || a.createdAt || 0)).slice(0, 5);
    $("#dashboard-products").innerHTML = recentProducts.length
        ? recentProducts.map(product => productRow(product)).join("")
        : '<tr><td colspan="6"><div class="empty-state">Todavía no hay artículos. Registra el primero para comenzar.</div></td></tr>';
}

function renderProducts() {
    const query = $("#product-search").value.trim().toLocaleLowerCase("es");
    const category = $("#category-filter").value;
    const filtered = state.products.filter(product => {
        const searchable = `${product.name} ${product.sku} ${product.category}`.toLocaleLowerCase("es");
        return searchable.includes(query) && (!category || product.category === category);
    });
    $("#products-table").innerHTML = filtered.length
        ? filtered.map(product => productRow(product, true)).join("")
        : '<tr><td colspan="7"><div class="empty-state">No hay artículos que coincidan con tu búsqueda.</div></td></tr>';
    $("#product-count").textContent = `${number.format(filtered.length)} de ${number.format(state.products.length)} artículos`;
    const categories = [...new Set(state.products.map(product => product.category))].sort((a, b) => a.localeCompare(b, "es"));
    const selected = $("#category-filter").value;
    $("#category-filter").innerHTML = '<option value="">Todas las categorías</option>' +
        categories.map(item => `<option value="${escapeHtml(item)}">${escapeHtml(item)}</option>`).join("");
    $("#category-filter").value = categories.includes(selected) ? selected : "";
}

function renderMovements() {
    $("#movements-table").innerHTML = state.movements.length
        ? state.movements.map(movementRow).join("")
        : '<tr><td colspan="7"><div class="empty-state">No hay movimientos para mostrar.</div></td></tr>';
}

async function refresh() {
    try {
        [state.products, state.dashboard, state.movements] = await Promise.all([
            api("/api/products"),
            api("/api/dashboard"),
            api(`/api/movements${$("#movement-filter").value ? `?type=${$("#movement-filter").value}` : ""}`)
        ]);
        renderDashboard();
        renderProducts();
        renderMovements();
        renderMovementProductOptions();
    } catch (error) {
        showNotice(error.message);
    }
}

function setView(view) {
    $$(".view").forEach(element => element.classList.toggle("active", element.id === `view-${view}`));
    $$(".nav-link").forEach(element => element.classList.toggle("active", element.dataset.view === view));
    const titles = { dashboard: "Resumen", products: "Artículos", movements: "Movimientos" };
    $("#current-section").textContent = titles[view];
    $("#page-title").innerHTML = view === "dashboard" ? 'Tu inventario, <span>bajo control.</span>' : `${titles[view]} <span>del almacén.</span>`;
    $("#page-subtitle").textContent = {
        dashboard: "Un vistazo claro a las existencias y la actividad de tu almacén.",
        products: "Administra artículos, precios y niveles mínimos de existencias.",
        movements: "Consulta las entradas y salidas registradas en tu inventario."
    }[view];
    $("#quick-movement").classList.toggle("hidden", view === "movements");
    $("#new-product").classList.toggle("hidden", view === "products");
}

function renderMovementProductOptions(selectedId = "") {
    const select = $("#movement-product");
    select.innerHTML = state.products.map(product => `<option value="${product.id}">${escapeHtml(product.name)} · ${escapeHtml(product.sku)} (${number.format(product.stock)} disp.)</option>`).join("");
    if (selectedId) select.value = String(selectedId);
    updateStockHint();
}

function updateStockHint() {
    const product = state.products.find(item => item.id === Number($("#movement-product").value));
    $("#movement-stock-hint").textContent = product
        ? `Existencias actuales: ${number.format(product.stock)} unidades · Precio de referencia: ${currency.format(product.unitPrice)}`
        : "Primero registra un artículo para poder crear movimientos.";
}

function openProductDialog(product = null) {
    const form = $("#product-form");
    form.reset();
    form.elements.id.value = product?.id || "";
    $("#product-dialog-title").textContent = product ? "Editar artículo" : "Nuevo artículo";
    $("#initial-stock-field").classList.toggle("hidden", Boolean(product));
    form.elements.stock.required = !product;
    if (product) {
        form.elements.name.value = product.name;
        form.elements.sku.value = product.sku;
        form.elements.category.value = product.category;
        form.elements.unitPrice.value = product.unitPrice;
        form.elements.lowStockThreshold.value = product.lowStockThreshold;
        form.elements.description.value = product.description || "";
    }
    $("#product-dialog").showModal();
}

function openMovementDialog(productId = "") {
    if (!state.products.length) {
        showNotice("Registra primero un artículo para poder crear movimientos.");
        openProductDialog();
        return;
    }
    $("#movement-form").reset();
    renderMovementProductOptions(productId);
    if (productId) $("#movement-form").elements.type.value = "ENTRY";
    $("#movement-dialog").showModal();
}

async function showHistory(product) {
    try {
        const movements = await api(`/api/products/${product.id}/movements`);
        $("#history-title").textContent = `Historial · ${product.name}`;
        $("#history-content").innerHTML = movements.length
            ? movements.map(movement => `<div class="history-row">
                <div><strong>${movement.type === "ENTRY" ? "Entrada" : "Salida"} · ${number.format(movement.quantity)} unidades</strong><br><small>${escapeHtml(dateTime(movement.createdAt))} · ${escapeHtml(movement.note || "Sin nota")}</small></div>
                <span class="badge ${movement.type === "ENTRY" ? "entry" : "exit"}">${currency.format(movement.totalValue)}</span>
            </div>`).join("")
            : '<div class="empty-state">Este artículo aún no tiene movimientos.</div>';
        $("#history-dialog").showModal();
    } catch (error) {
        showNotice(error.message);
    }
}

async function handleProductAction(event) {
    const button = event.target.closest("[data-action]");
    if (!button) return;
    const product = state.products.find(item => item.id === Number(button.dataset.id));
    if (!product) return;
    if (button.dataset.action === "entry" || button.dataset.action === "exit") {
        openMovementDialog(product.id);
        if (button.dataset.action === "exit") $("#movement-form").elements.type.value = "EXIT";
    } else if (button.dataset.action === "edit") {
        openProductDialog(product);
    } else if (button.dataset.action === "history") {
        await showHistory(product);
    } else if (button.dataset.action === "delete") {
        if (!window.confirm(`¿Eliminar "${product.name}"? Esta acción no se puede deshacer.`)) return;
        try {
            await api(`/api/products/${product.id}`, { method: "DELETE" });
            showNotice("Artículo eliminado correctamente.", "success");
            await refresh();
        } catch (error) {
            showNotice(error.message);
        }
    }
}

$("#product-form").addEventListener("submit", async event => {
    event.preventDefault();
    const form = event.currentTarget;
    const data = Object.fromEntries(new FormData(form));
    const id = data.id;
    delete data.id;
    data.unitPrice = Number(data.unitPrice);
    data.stock = Number(data.stock || 0);
    data.lowStockThreshold = Number(data.lowStockThreshold);
    try {
        await api(id ? `/api/products/${id}` : "/api/products", {
            method: id ? "PUT" : "POST", body: JSON.stringify(data)
        });
        $("#product-dialog").close();
        showNotice(id ? "Artículo actualizado correctamente." : "Artículo creado correctamente.", "success");
        await refresh();
    } catch (error) {
        showNotice(error.message);
    }
});

$("#movement-form").addEventListener("submit", async event => {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(event.currentTarget));
    const productId = data.productId;
    data.quantity = Number(data.quantity);
    try {
        await api(`/api/products/${productId}/movements`, { method: "POST", body: JSON.stringify(data) });
        $("#movement-dialog").close();
        showNotice(`${data.type === "ENTRY" ? "Entrada" : "Salida"} registrada correctamente.`, "success");
        await refresh();
    } catch (error) {
        showNotice(error.message);
    }
});

$$(".nav-link").forEach(button => button.addEventListener("click", () => setView(button.dataset.view)));
$$("[data-go]").forEach(button => button.addEventListener("click", () => setView(button.dataset.go)));
$("#new-product").addEventListener("click", () => openProductDialog());
$("#products-add").addEventListener("click", () => openProductDialog());
$("#quick-movement").addEventListener("click", () => openMovementDialog());
$("#movement-add").addEventListener("click", () => openMovementDialog());
$("#product-search").addEventListener("input", renderProducts);
$("#category-filter").addEventListener("change", renderProducts);
$("#movement-filter").addEventListener("change", async () => {
    try {
        state.movements = await api(`/api/movements${$("#movement-filter").value ? `?type=${$("#movement-filter").value}` : ""}`);
        renderMovements();
    } catch (error) {
        showNotice(error.message);
    }
});
$("#movement-product").addEventListener("change", updateStockHint);
$("#products-table").addEventListener("click", handleProductAction);
$$("[data-close]").forEach(button => button.addEventListener("click", () => button.closest("dialog").close()));
$$("dialog").forEach(dialog => dialog.addEventListener("click", event => {
    if (event.target === dialog) dialog.close();
}));

$("#today-label").textContent = new Intl.DateTimeFormat("es-CO", { weekday: "short", day: "numeric", month: "short", year: "numeric" }).format(new Date());
refresh();
