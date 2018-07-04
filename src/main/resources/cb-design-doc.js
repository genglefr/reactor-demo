//_design/pet/all
function (doc, meta) {
    if(doc._class === "com.genglefr.webflux.demo.model.Pet"){
        emit(meta.id, null);
    }
}
//_design/user/all
function (doc, meta) {
    if(doc._class === "com.genglefr.webflux.demo.model.User"){
        emit(meta.id, null);
    }
}