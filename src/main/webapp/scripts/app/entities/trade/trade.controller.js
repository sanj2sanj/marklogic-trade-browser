'use strict';

angular.module('tradebrowserApp')
    .controller('TradeController', function ($scope, Trade, TradeSearch, ParseLinks) {
        $scope.trades = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Trade.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.trades.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.trades = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Trade.get({id: id}, function(result) {
                $scope.trade = result;
                $('#saveTradeModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.trade.id != null) {
                Trade.update($scope.trade,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Trade.save($scope.trade,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Trade.get({id: id}, function(result) {
                $scope.trade = result;
                $('#deleteTradeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Trade.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteTradeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TradeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.trades = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveTradeModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.trade = {stock: null, tradeDate: null, settlementDate: null, amount: null, currency: null, counterparty: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
