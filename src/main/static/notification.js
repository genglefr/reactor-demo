if (window.Notification) {
    if (Notification.permission !== 'denied') {
        Notification.requestPermission();
    }
}
function notify(data, action) {
    if (window.Notification && Notification.permission === "granted") {
        var body = data.toString(true);
        var n = new Notification("GOOOAAAL !", {
            body: body ? body : "",
            icon: "/icon.png",
            tag: data.id,
            requireInteraction: true
        });
        n.onclick = function (event) {
            if (highlight) {
                var object = document.querySelector("#id-" + data.id);
                highlight(object);
            }
            parent.focus();
            window.focus();
            this.close();
        }
    }
}