'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:FlashCtrl
 * @description
 * # FlashCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('FlashCtrl', ['$scope','flash', function ($scope, flash) {
    $scope.flash = flash;
  }]);
