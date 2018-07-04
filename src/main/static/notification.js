if (window.Notification) {
    if (Notification.permission !== 'denied') {
        Notification.requestPermission();
    }
}
function notify(data, action) {
    if (window.Notification && Notification.permission === "granted") {
        var body = data.toString();
        var n = new Notification("Data " + (action === "U" ? "updated" : "deleted") + " !", {
            body: body ? body : "",
            icon: "/icon.png",
            tag: data.id
        });
        n.onclick = function (event) {
            if (highlight) {
                var object = document.querySelector("#id-" + this.tag);
                highlight(object);
            }
            parent.focus();
            window.focus();
            this.close();
        };
    }
}