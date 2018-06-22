initObjects("User", "/users");
initObjects("Pet", "/pets");

function initObjects(type, path){
    makeRequest("GET", path).then(function(objects) {
        var container = document.querySelector("[type=" + type + "]");
        for (var i = 0; i < objects.length; i++) {
            display(container, objects[i], "C");
        }
    });
}

var source = new EventSource("/events");
source.onmessage = function(event) {
    var eventData = JSON.parse(event.data);
    var action = eventData.operationType === "C" ? "created" :
        (eventData.operationType === "U" ? "updated" : "deleted");
    notify(eventData.resourceType + " " + action + " !", event.data);
    var container = document.querySelector("[type=" + eventData.resourceType + "]");
    display(container, eventData.resource, eventData.operationType);
};
source.onerror = function(event) {
    console.log(event);
};

function display(container, data, operationType, f){
    var object = container.querySelector("#id-" + data.id);
    if (operationType === "D") {
        if (object)
            object.remove();
    } else {
        if (!object) {
            object = document.createElement("li");
            object.id = "id-" + data.id;
            container.appendChild(object);
        }
        object.textContent = toString(data);
    }
}

function toString(data) {
    if (data.resourceType === "User")
        return "Firstname: " + data.firstname
            + ", Lastname: " + data.lastname
            + ", Age: " + data.age;
    if (data.resourceType === "Pet")
        return "Name: " + data.name
            + ", Type: " + data.type;
};