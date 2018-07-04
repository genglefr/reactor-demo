function OnUpdate(doc, meta) {
    doc.id = meta.id;
    var response = curl("http://localhost:8080/event/".concat(meta.id), { method: "POST", data: doc });
}
function OnDelete(meta) {
    var response = curl("http://localhost:8080/event/".concat(meta.id), { method: "POST", data: {id: meta.id} });
}