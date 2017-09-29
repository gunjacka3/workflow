(function (s) {
    s.parseJSON = function() {
        return JSON.parse(this);
    };

    s.toJSONString = function () {
        return JSON.stringify(this);
    };
})(String.prototype);