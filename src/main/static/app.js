initObjects("Game", "/games");

function initObjects(type, path){
    httprequest("GET", path).then(function(objects) {
        for (var i = 0; i < objects.length; i++) {
            objects[i].class = type;
            objects[i].toString = toString;
            display(objects[i], "C");
        }
    });
}

var toString = function(avoidHTML) {
    if (this.class === "Game")
        if (avoidHTML)
            return this.teamHome + ' ' + this.teamHomeScore + '-' + this.teamAwayScore + ' ' + this.teamAway;
        return '<div class="left"><img src="./team-flags/'+this.teamHome+'.png" />' + this.teamHome + '</div>'
            + '<div class="right">' + this.teamAway + '<img src="./team-flags/'+this.teamAway+'.png" /></div>'
            + '<div class="center">'+getNumberIcon(this.teamHomeScore) + getNumberIcon(this.teamAwayScore) + '</div>';
};

if (!window.EventSource) {
    loadScript("./eventsource.js").then(function() {
        createEventSource();
    });
} else {
    createEventSource();
}

function createEventSource() {
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
        object.innerHTML  = data.toString();
    }
}

function highlight(object) {
    if (object) {
        var transition = object.style.transition;
        var backgroundColor = object.style.backgroundColor;
        object.style.transition = "unset";
        object.style.backgroundColor = "#ff6666";
        window.setTimeout(function() {
            window.requestAnimationFrame(function() {
                object.style.transition = transition;
                object.style.backgroundColor = backgroundColor;
            });
        }, 800);
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