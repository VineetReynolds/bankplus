'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('LoginCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
      var usersInStore = localStorageService.get('users');

      $scope.users = usersInStore || [];

      $scope.loginUser = function() {
        var credentials = {'email':$scope.login.email, 'password':$scope.login.password};
        for(var ctr = 0; ctr < $scope.users.length; ctr++) {
          var user = $scope.users[ctr];
          if(credentials.email === user.email && credentials.password === user.password) {
            flash.setMessage({'type':'success','text':'You have logged in successfully.'});
            $location.path('/customers/dashboard');
            return;
          }
        }
        flash.setMessage({'type':'error','text':'Email or password was incorrect.'});
      };
  }]);
