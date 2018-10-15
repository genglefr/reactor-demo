function OnUpdate(doc, meta) {
    doc.id = meta.id;
    curl("http://localhost:8080/event/".concat(meta.id).concat("?class=").concat(doc._class), {
        method: "POST",
        data: doc
    });
}
function OnDelete(meta) {}