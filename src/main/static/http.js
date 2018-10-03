function httprequest(method, url, data) {
    var data = data || '';
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.open(method, url);
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