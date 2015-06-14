'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsDepositsNewCtrl
 * @description
 * # TransactionsDepositsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsDepositsNewCtrl', ['$scope', '$location', 'flash', 'depositResource', function ($scope, $location, flash, depositResource) {

    $scope.makeDeposit = function() {
      var transactionToStore = {'amount':$scope.deposit.amount};
      var successCallback = function(data,responseHeaders){
        flash.setMessage({'type':'success','text':'Your account has been credited.'});
        $location.path('/customers/dashboard');
      };
      var errorCallback = function() {
        $scope.displayError = true;
      };
      depositResource.save({'customerId':auth.customer.id}, transactionToStore, successCallback, errorCallback);
    };

    $scope.clear = function() {
      $scope.deposit = {};
    };
  }]);
