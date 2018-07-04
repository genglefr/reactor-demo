initObjects("User", "/users");
initObjects("Pet", "/pets");

function initObjects(type, path){
    makeRequest("GET", path).then(function(objects) {
        for (var i = 0; i < objects.length; i++) {
            objects[i].class = type;
            objects[i].toString = toString;
            display(objects[i], "C", type);
        }
    });
}

if (!window.EventSource) {
    alert("Update your browser");
} else {
    var source = new EventSource("/events");
    source.onmessage = function(event) {
        console.log(event.data);
        var eventData = JSON.parse(event.data);
        var action = !eventData._class ? "D" : "U";
        eventData.class = eventData._class ? eventData._class.split('.').pop() : undefined;
        eventData.toString = toString;
        notify(eventData, action);
        display(eventData, action, eventData);
    };
    source.onerror = function(event) {
        console.log(event);
    };
}

function display(data, action){
    var object = document.querySelector("#id-" + data.id);
    if (action === "D") {
        if (object)
            object.remove();
    } else {
        if (!object) {
            var container = document.querySelector("[type=" + data.class + "]");
            object = document.createElement("li");
            object.id = "id-" + data.id;
            container.appendChild(object);
        }
        object.textContent = data;
    }
}

var toString = function() {
    if (this.class === "User")
        return "Firstname: " + this.firstname
            + ", Lastname: " + this.lastname
            + ", Age: " + this.age;
    if (this.class === "Pet")
        return "Name: " + this.name
            + ", Type: " + this.type;
};