'use strict';

angular.module('tradebrowserApp')
    .factory('Trade', function ($resource) {
        return $resource('api/trades/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.tradeDate != null){
                        var tradeDateFrom = data.tradeDate.split("-");
                        data.tradeDate = new Date(new Date(tradeDateFrom[0], tradeDateFrom[1] - 1, tradeDateFrom[2]));
                    }
                    if (data.settlementDate != null){
                        var settlementDateFrom = data.settlementDate.split("-");
                        data.settlementDate = new Date(new Date(settlementDateFrom[0], settlementDateFrom[1] - 1, settlementDateFrom[2]));
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
