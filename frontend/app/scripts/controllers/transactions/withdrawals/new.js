'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsWithdrawalsNewCtrl
 * @description
 * # TransactionsWithdrawalsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsWithdrawalsNewCtrl', ['$scope', '$location', 'flash', 'withdrawalResource', function ($scope, $location, flash, withdrawalResource) {
    $scope.makeWithdrawal = function() {
      var transactionToStore = {'amount':$scope.withdrawal.amount};
      var successCallback = function(data,responseHeaders){
        flash.setMessage({'type':'success','text':'Your account has been debited.'});
        $location.path('/customers/dashboard');
      };
      var errorCallback = function(httpResponse) {
        flash.setMessage({'type':'danger','text':httpResponse.data.message}, true);
        $scope.displayError = true;
      };
      withdrawalResource.save({'customerId':auth.customer.id}, transactionToStore, successCallback, errorCallback);
    };

    $scope.clear = function() {
      $scope.withdrawal = {};
    };
  }]);
