makeRequest("GET", "./users").then(function(users) {
    var container = document.querySelector("[type=User]");
    for (var i = 0; i < users.length; i++) {
        display(container, users[i], "C");
    }
});

var source = new EventSource("./users/events");
source.onmessage = function(event) {
    if (Notification.permission === "granted") {
        var n = new Notification("Data updated !", {
            body: event.data,
            icon: "/icon.png"
        });
        n.onclick = function(event) {
            parent.focus();
            window.focus();
            this.close();
        };
    }
    var eventData = JSON.parse(event.data);
    var container = document.querySelector("[type=" + eventData.resourceType + "]");
    display(container, eventData.resource, eventData.operationType);
};
source.onerror = function(event) {
    console.log(event);
};

function display(container, data, operationType){
    var user = container.querySelector("#id-" + data.id);
    if (operationType === "D") {
        if (user)
            user.remove();
    } else {
        if (!user) {
            user = document.createElement("li");
            user.id = "id-" + data.id;
            container.appendChild(user);
        }
        user.textContent = "Firstname: " + data.firstname
            + ", Lastname: " + data.lastname
            + ", Age: " + data.age;
    }
}
function makeRequest(method,url,data) {
    var data = data || '';
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.open(method, url);
        req.onload = function() {
            if (req.status === 200) {
                resolve(JSON.parse(req.response));
            }
            else {
                reject(Error(req.statusText));
            }
        };
        req.onerror = function() {
            reject(req);
        };
        req.send(data);
    });
}
if (Notification.permission !== 'denied') {
    Notification.requestPermission();
}