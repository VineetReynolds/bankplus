'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('MainCtrl', ['$scope', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  }]);
