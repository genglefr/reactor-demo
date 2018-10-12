if (window.Notification) {
    if (Notification.permission !== 'denied') {
        Notification.requestPermission();
    }
}

function notify(data) {
    if (window.Notification && Notification.permission === "granted") {
        var body = data.print(true);
        var n = new Notification("GOOOAAAL !", {
            body: body ? body : "",
            icon: "/images/notification.png",
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