'use strict';

angular.module('tradebrowserApp')
    .factory('TradeSearch', function ($resource) {
        return $resource('api/_search/trades/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
