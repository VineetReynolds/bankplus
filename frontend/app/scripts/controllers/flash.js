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
    $scope.showAlert = false;

    $scope.$watch('flash.getMessage()', function(newVal) {
      var message = newVal;
      if(message && message.text) {
        $scope.showAlert = message.text.length > 0;
      } else {
        $scope.showAlert = false;
      }
    });

    $scope.hideAlert = function() {
      $scope.showAlert = false;
    }
  }]);
