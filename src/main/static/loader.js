function loadScript(url) {
    var script = document.createElement("script");
    script.src = url;
    document.head.appendChild(script);
    return new Promise(function(resolve, reject) {
        script.onload = function() {
            console.log("Script loaded: '" + url + "'");
            resolve();
        };
    });
}