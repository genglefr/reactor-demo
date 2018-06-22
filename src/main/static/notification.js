if (Notification.permission !== 'denied') {
    Notification.requestPermission();
}
function notify(title, body) {
    if (Notification.permission === "granted") {
        var n = new Notification(title, {
            body: body,
            icon: "/icon.png"
        });
        n.onclick = function (event) {
            parent.focus();
            window.focus();
            this.close();
        };
    }
}