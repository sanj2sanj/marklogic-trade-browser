'use strict';

angular.module('tradebrowserApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
