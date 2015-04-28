'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('DashboardCtrl', ["$scope", function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  }]);
