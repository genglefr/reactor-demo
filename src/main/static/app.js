//initObjects("User", "/users");
initObjects("Pet", "/pets");

function initObjects(type, path){
    makeRequest("GET", path).then(function(objects) {
        var container = document.querySelector("[type=" + type + "]");
        for (var i = 0; i < objects.length; i++) {
            display(objects[i], "C");
        }
    });
}

var source = new EventSource("/events");
source.onmessage = function(event) {
    console.log(event.data);
    var eventData = JSON.parse(event.data);
    var action = !eventData._class ? "D" : "U";
    notify(eventData, action);
    display(eventData, action);
};
source.onerror = function(event) {
    console.log(event);
};

function display(data, action){
    var object = document.querySelector("#id-" + data.id);
    if (action === "D") {
        if (object)
            object.remove();
    } else {
        if (!object) {
            var container = document.querySelector("[type=Pet]")
            object = document.createElement("li");
            object.id = "id-" + data.id;
            container.appendChild(object);
        }
        object.textContent = toString(data);
    }
}

function toString(data) {
    /*if (data._class.split('.').pop() === "User")
        return "Firstname: " + data.firstname
            + ", Lastname: " + data.lastname
            + ", Age: " + data.age;
    if (data._class.split('.').pop() === "Pet")*/
        return "Name: " + data.name
            + ", Type: " + data.type;
};