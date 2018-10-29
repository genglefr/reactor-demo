function httprequest(method, url, data) {
    return new Promise(function(resolve, reject) {
        var req = new XMLHttpRequest();
        req.open(method, url);
        req.onload = function() {
            if (req.status === 200) {
                resolve(req.response ? JSON.parse(req.response) : undefined);
            } else {
                reject(Error(req.statusText));
            }
        };
        req.onerror = function() {
            reject(req);
        };
        if (data) req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        data = data ? JSON.stringify(data) : '';
        req.send(data);
    });
}