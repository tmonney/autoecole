/*global alert, require, requirejs */

requirejs.config({
    paths: {
        'underscore': '../lib/underscorejs/underscore'
    },
    shim: {
        'underscore': {
            exports: '_'
        }
    }
});

require(["underscore"], function (_) {
    _.each([1, 2, 3], alert);
});
