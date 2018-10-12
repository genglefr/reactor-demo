var teamDb = (function () {
            var tDB = {};
            var datastore = null;

            tDB.getDatastore = function () {
                var p = new Promise(function (resolve, reject) {
                    if (datastore) resolve(datastore);
                    else {
                        tDB.open().then(function () {
                            resolve(datastore);
                        }).catch(function (e) {
                            reject(e);
                        });
                    }
                });
                return p;
            }

            tDB.open = function () {
                return new Promise(function (resolve, reject) {
                    var version = 1;
                    var request = indexedDB.open('teams', version);
                    request.onupgradeneeded = function (e) {
                        var db = e.target.result;
                        e.target.transaction.onerror = tDB.onerror;
                        if (db.objectStoreNames.contains('team')) {
                            db.deleteObjectStore('team');
                        }
                        var store = db.createObjectStore('team', {keyPath: 'id', autoIncrement: true});
                        store.createIndex('name', 'name', {unique: true});
                    };
                    request.onsuccess = function (e) {
                        datastore = e.target.result;
                        resolve();
                    };
                    request.onerror = function (e) {
                        reject(e);
                    };
                });
            };

            tDB.getAll = function () {
                return new Promise(function (resolve, reject) {
                    tDB.getDatastore().then(function (db) {
                        var transaction = db.transaction(['team'], 'readwrite');
                        var objStore = transaction.objectStore('team');
                        var keyRange = IDBKeyRange.lowerBound(0);
                        var cursorRequest = objStore.openCursor(keyRange);
                        var teams = [];
                        transaction.oncomplete = function (e) {
                            resolve(teams);
                        };
                        cursorRequest.onsuccess = function (e) {
                            var result = e.target.result;
                            if (!!result == false) {
                                return;
                            }
                            teams.push(result.value);
                            result.continue();
                        };
                        cursorRequest.onerror = function (e) {
                            reject(e);
                        };
                    });
                });
            }

            tDB.createTeam = function (name, callback) {
                return new Promise(function (resolve, reject) {
                    tDB.getDatastore().then(function (db) {
                        var transaction = db.transaction(['team'], 'readwrite');
                        var objStore = transaction.objectStore('team');
                        var request = objStore.put({
                            'name': name
                        });
                        request.onsuccess = function (e) {
                            resolve();
                        };
                        request.onerror = function (e) {
                            reject(e);
                        };
                    });
                });
            }

            tDB.deleteTeam = function (name) {
                return new Promise(function (resolve, reject) {
                    tDB.getAll().then((function (teams) {
                        var id;
                        teams.forEach(function (team) {
                            if (team.name === name) id = team.id;
                        });
                        tDB.deleteTeamById(id).then(function () {
                            resolve();
                        }).catch(function (e) {
                            reject(e);
                        });
                    }));
                });
            }

            tDB.deleteTeamById = function (id) {
                return new Promise(function (resolve, reject) {
                    tDB.getDatastore().then(function (db) {
                        var transaction = db.transaction(['team'], 'readwrite');
                        var objStore = transaction.objectStore('team');
                        var request = objStore.delete(id);
                        request.onsuccess = function (e) {
                            resolve();
                        }
                        request.onerror = function (e) {
                            reject(e);
                        }
                    });
                });
            }

            return tDB;
        }
        ()
    )
;