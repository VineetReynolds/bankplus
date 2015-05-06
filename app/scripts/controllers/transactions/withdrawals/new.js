'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsWithdrawalsNewCtrl
 * @description
 * # TransactionsWithdrawalsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsWithdrawalsNewCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
    var transactionsInStore = localStorageService.get('transactions');

    $scope.transactions = transactionsInStore || [];

    $scope.$watch('transactions', function(){
      localStorageService.set('transactions', $scope.transactions);
    }, true);

    $scope.makeWithdrawal = function() {
      var transactionToStore = {'type':'WITHDRAWAL', 'amount':$scope.withdrawal.amount};
      $scope.transactions.push(transactionToStore);
      flash.setMessage({'type':'success','text':'Your account has been debited.'});
      $location.path('/customers/dashboard');
    };

    $scope.clear = function() {
      $scope.withdrawal = {};
    };
  }]);
