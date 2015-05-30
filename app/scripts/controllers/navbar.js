'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('NavbarCtrl', ['$scope', function ($scope) {
    $scope.logout = logout;
  }]);
