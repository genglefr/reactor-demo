if (Notification.permission !== 'denied') {
    Notification.requestPermission();
}
function notify(data, action) {
    if (Notification.permission === "granted") {
        var n = new Notification("Data " + (action === "U" ? "updated" : "deleted") + " !", {
            body: data,
            icon: "/icon.png"
        });
        n.onclick = function (event) {
            parent.focus();
            window.focus();
            this.close();
        };
    }
}