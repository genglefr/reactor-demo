httprequest("GET", "/deployment-info").then(function (info) {
    info.applicationHostAddress = "127.0.0.1";
    var location = (info.applicationPort == 8080 ? "http://" : "https://") + info.applicationHostAddress + ":" + info.applicationPort;
    var link = document.querySelector("[class=deployment-info]");
    link.href = location;
    link.innerHTML = location;
});

if (!window.EventSource) {
    loadScript("./eventsource.js").then(function () {
        afterEventSourceScriptLoad();
    });
} else {
    afterEventSourceScriptLoad();
}

function afterEventSourceScriptLoad() {
    createEventSource().then(function () {
        initObjects("Game", "/games")
    });
    createCounterEventSource();
}

function initObjects(type, path) {
    httprequest("GET", path).then(function (objects) {
        for (var i = 0; i < objects.length; i++) {
            objects[i].print = print;
            display(objects[i]);
        }
    });
}

var print = function (avoidHTML) {
    if (avoidHTML)
        return this.teamHome + ' ' + this.teamHomeScore + '-' + this.teamAwayScore + ' ' + this.teamAway;
    var homeTeamClass = window.teams.includes(this.teamHome) ? "left fav" : "left";
    var awayTeamClass = window.teams.includes(this.teamAway) ? "right fav" : "right";
    return '<div onclick="toggleFavouriteTeam(\'' + this.teamHome + '\',this)" class="' + homeTeamClass + '"><img src="images/team-flags/' + this.teamHome + '.png" /><span>' + this.teamHome + '</span></div>'
        + '<div onclick="toggleFavouriteTeam(\'' + this.teamAway + '\',this)" class="' + awayTeamClass + '"><span>' + this.teamAway + '</span><img src="images/team-flags/' + this.teamAway + '.png" /></div>'
        + '<div class="center">' + getNumberIcon(this.teamHomeScore) + getNumberIcon(this.teamAwayScore) + '</div>';
};

function createEventSource() {
    return new Promise(function (resolve, reject) {
        getFavoriteTeams().then(function () {
            var sourceUrl = "/event/fav/game" + (teams.length > 0 ? "?fav=" + teams.join(',') : "");
            //var sourceUrl = "/event/game";
            if (window.source) window.source.close();
            window.source = new EventSource(sourceUrl);
            window.source.onmessage = function (event) {
                var eventData = JSON.parse(event.data);
                eventData.print = print;
                notify(eventData);
                display(eventData, true);
            };
            window.source.onerror = function (event) {
                if (window.source.readyState == 2) {
                    setTimeout(createEventSource, 5000);
                }
            };
            window.onbeforeunload = function () {
                window.source.close();
            }
            resolve();
        }).catch(function (e) {
            reject(e);
        });
    });
}

function createCounterEventSource() {
    if (window.counterSource) window.counterSource.close();
    window.counterSource = new EventSource("event/subscription/count");
    window.counterSource.onmessage = function (event) {
        var eventData = JSON.parse(event.data);
        var container = document.querySelector("[class=counter]");
        if (container.innerHTML) highlight(container, "highlight_black");
        container.innerHTML = "#" + eventData;
    };
    window.counterSource.onerror = function (event) {
        if (window.counterSource.readyState == 2) {
            setTimeout(createCounterEventSource, 5000);
        }
    };
    window.onbeforeunload = function () {
        window.counterSource.close();
    }
}

function getFavoriteTeams() {
    return new Promise(function (resolve, reject) {
        teamDb.getAll().then(function (teams) {
            var result = [];
            teams.forEach(function (team) {
                result.push(team.name);
            });
            window.teams = result;
            resolve();
        }).catch(function (e) {
            reject(e);
        });
    });
}

function toggleFavouriteTeam(name, div) {
    if (div.classList.contains("fav")) {
        teamDb.deleteTeam(name).then(function () {
            div.classList.remove("fav");
            createEventSource();
        }).catch(function (e) {
            console.log(e);
        });
    } else {
        teamDb.createTeam(name).then(function () {
            div.classList.add("fav");
            createEventSource();
        }).catch(function (e) {
            console.log(e);
        });
    }
}

function display(data, isEventSource) {
    var object = document.querySelector("#id-" + data.id);
    if (!object) {
        var container = document.querySelector("[type=Game]");
        object = document.createElement("li");
        object.id = "id-" + data.id;
        container.appendChild(object);
    }
    object.innerHTML = data.print();
    if (isEventSource) {
        highlight(object);
    }
}

function highlight(object, style) {
    if (!style) style = "highlight";
    if (object) {
        object.style.animation = "unset";
        void object.offsetWidth;
        object.style.animation = style + " 5s";
    }
}

function getNumberIcon(num) {
    var icon = [];
    var numString = num.toString();
    for (var i = 0; i < numString.length; i++) {
        icon.push(numString[i] + "️⃣");
    }
    return icon.join('');
}