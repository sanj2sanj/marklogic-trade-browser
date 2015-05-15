'use strict';

angular.module('tradebrowserApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


