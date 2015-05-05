'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('RegisterCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
    var usersInStore = localStorageService.get('users');

    $scope.users = usersInStore || [];

    $scope.$watch('users', function(){
      localStorageService.set('users', $scope.users);
    }, true);

    $scope.registerUser = function() {
      var userToStore = {'email':$scope.user.email, 'password':$scope.user.password};
      $scope.users.push(userToStore);
      flash.setMessage({'type':'success','text':'You have been registered successfully.'});
      $location.path('/login');
    };

    $scope.clearUser = function() {
      $scope.user = {};
    };
  }]);
