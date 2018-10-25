function httprequest(method, url, data) {
    var data = data ? JSON.stringify(data) : '';
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.open(method, url);
        if (data) req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        req.onload = function() {
            if (req.status === 200) {
                resolve(JSON.parse(req.response));
            } else {
                reject(Error(req.statusText));
            }
        };
        req.onerror = function() {
            reject(req);
        };
        req.send(data);
    });
}