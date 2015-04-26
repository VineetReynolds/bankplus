'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('RegisterCtrl', ['$scope', '$location', 'localStorageService', function ($scope, $location, localStorageService) {
    var usersInStore = localStorageService.get('users');

    $scope.users = usersInStore || [];

    $scope.$watch('users', function(){
      localStorageService.set('users', $scope.users);
    }, true);

    $scope.registerUser = function() {
      $scope.users.push($scope.user);
      flash.setMessage({"type":"success","text":"You have been registered successfully."});
      $location.path("/login");
    };

    $scope.clearUser = function() {
      $scope.user = {};
    };
  }]);
