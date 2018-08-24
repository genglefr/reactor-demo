//initObjects("User", "/users");
//initObjects("Pet", "/pets");
initObjects("Game", "/games");

function initObjects(type, path){
    makeRequest("GET", path).then(function(objects) {
        for (var i = 0; i < objects.length; i++) {
            objects[i].class = type;
            objects[i].toString = toString;
            display(objects[i], "C");
        }
    });
}

if (!window.EventSource) {
    alert("Update your browser");
} else {
    var source = new EventSource("/events");
    source.onmessage = function(event) {
        var eventData = JSON.parse(event.data);
        var action = eventData._class ? "U" : "D";
        eventData.class = eventData._class ? eventData._class.split('.').pop() : undefined;
        eventData.toString = toString;
        notify(eventData, action);
        display(eventData, action, true);
    };
    source.onerror = function(event) {
        console.log(event);
    };
}

function display(data, action, isEventSource){
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
        if (isEventSource) {
            highlight(object);
        }
        object.innerHTML  = data;
    }
}

function highlight(object) {
    if (object) {
        var transition = object.style.transition;
        var backgroundColor = object.style.backgroundColor;
        object.style.transition = "unset";
        object.style.backgroundColor = "lightskyblue";
        window.setTimeout(function() {
            object.style.transition = transition;
            object.style.backgroundColor = backgroundColor;
        }, 500);
    }
}

var toString = function(avoidHTML) {
    if (this.class === "User")
        return getNumberIcon(this.age) + " " + this.firstname
            + " " + this.lastname;
    if (this.class === "Pet")
        return getPetIcon(this.type) + " " + this.name;
    if (this.class === "Game")
        return (!avoidHTML?'<img src="./team-flags/'+this.teamHome+'.png" />':'') +
            this.teamHome + ' ' + (!avoidHTML?getNumberIcon(this.teamHomeScore):this.teamHomeScore) + (avoidHTML?'-':'') + (!avoidHTML?getNumberIcon(this.teamAwayScore):this.teamAwayScore) + ' ' + this.teamAway +
            (!avoidHTML?'<img src="./team-flags/'+this.teamAway+'.png" />':'');
};

function getPetIcon(type){
    switch(type) {
        case "CAT":
            return "😺";
        case "DOG":
            return "🐶";
        case "FISH":
            return "🐠";
    }
}

function getNumberIcon(num){
    var icon = [];
    var numString = num.toString();
    for(var i = 0; i < numString.length; i++) {
        icon.push(numString[i]+"️⃣");
    }
    return icon.join('');
}