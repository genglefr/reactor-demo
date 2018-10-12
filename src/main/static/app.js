if (!window.EventSource) {
    loadScript("./eventsource.js").then(function () {
        createEventSource().then(function () {
            initObjects("Game", "/games")
        });
    });
} else {
    createEventSource().then(function () {
        initObjects("Game", "/games")
    });
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
    var homeTeamClass = window.teams.includes(this.teamHome) ? "left favourite" : "left";
    var awayTeamClass = window.teams.includes(this.teamAway) ? "right favourite" : "right";
    return '<div onclick="toggleFavouriteTeam(\'' + this.teamHome + '\',this)" class="' + homeTeamClass + '"><img src="images/team-flags/' + this.teamHome + '.png" />' + this.teamHome + '</div>'
        + '<div onclick="toggleFavouriteTeam(\'' + this.teamAway + '\',this)" class="' + awayTeamClass + '">' + this.teamAway + '<img src="images/team-flags/' + this.teamAway + '.png" /></div>'
        + '<div class="center">' + getNumberIcon(this.teamHomeScore) + getNumberIcon(this.teamAwayScore) + '</div>';
};

function createEventSource() {
    return new Promise(function (resolve, reject) {
        getFavoriteTeams().then(function () {
            var sourceUrl = "/events" + (teams.length > 0 ? "?teams=" + teams.join(',') : "");
            //var sourceUrl = "/events/all";
            if (window.source) window.source.close();
            window.source = new EventSource(sourceUrl);
            window.source.onmessage = function (event) {
                var eventData = JSON.parse(event.data);
                eventData.print = print;
                notify(eventData);
                display(eventData, true);
            };
            window.source.onerror = function (event) {
                console.log(event);
            };
            resolve();
        }).catch(function (e) {
            reject(e);
        });
    });
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
    if (div.classList.contains("favourite")) {
        teamDb.deleteTeam(name).then(function () {
            div.classList.remove("favourite");
            createEventSource();
        }).catch(function (e) {
            console.log(e);
        });
    } else {
        teamDb.createTeam(name).then(function () {
            div.classList.add("favourite");
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

function highlight(object) {
    if (object) {
        object.style.animation = "unset";
        void object.offsetWidth;
        object.style.animation = "highlight 5s";
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