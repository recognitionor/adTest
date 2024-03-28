var MRAID_ENV = {};
var listeners = [];
listeners['stateChange'] = [];
var state = 'loading'; //Can be loading, default, expanded, hidden, or resized

var mraid = {
    nativeCall: function (url) {
        window.location = url
    },

    open: function () {

    },

    fireEvent: function (event) {
        // this.nativeCall("mraid://test");
        document.location = "mraid://showToast";
        // Device.showToast("32");
        console.log("fireEvent : " + event);
        if (!listeners[event]) {
            console.log("1");
            return;
        }
        console.log("2");
        var args = Array.prototype.slice.call(arguments);
        args.shift();
        var length = listeners[event].length;
        console.log("length : " + length);
        for (var i = 0; i < length; i++) {
            console.log("for : " + i);
            if (typeof listeners[event][i] === "function") {
                console.log("3");
                listeners[event][i].apply(null, args);
            }
        }
    },

    testPrint: function () {
        console.log("텔미텔미 테테테테레레레텔미~~");
        return "텔미텔미 테테테테레레레텔미"
    },

    open: function (url) {
        // this.util.fireEvent("test")
        console.log("open  : " + MRAID_ENV.version);
        Device.open(url);
        console.log("open  : " + MRAID_ENV.version);
    },

    getVersion: function () {
        return MRAID_ENV.version;
    },

    getState: function () {
    },

    addEventListener: function (event_name, method) {
        if (listeners) {
            console.log("1 : " + listeners)
        } else {
            console.log("2 : " + listeners)
        }
        console.log("addEventListener : " + listeners)
        console.log("addEventListener : " + listeners.length)
        if (!listeners) return; // Listener is already registered
        console.log("event_name : " + event_name)
        if (event_name == 'stateChange') {
            console.log("stateChange!!!")
        }
        listeners[event_name].push(method);
    },

    removeEventListener: function (event_name, method) {
        //If no method name is given, remove all listeners from event
        if (method == null) {
            listeners[event_name].length = 0;
            return;
        }

        var method_index = listeners[event_name].indexOf(method);
        if (method_index > -1) { //Don't try to remove unregistered listeners
            listeners[event_name].splice(method_index, 1);
        } else {
            mraid.util.errorEvent("An unregistered listener was requested to be removed.", "mraid.removeEventListener()")
        }
    }, onReadyEvent: function () {
        console.log("onReady")
    }

};
