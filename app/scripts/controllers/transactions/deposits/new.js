'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsDepositsNewCtrl
 * @description
 * # TransactionsDepositsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsDepositsNewCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
    var transactionsInStore = localStorageService.get('transactions');

    $scope.transactions = transactionsInStore || [];

    $scope.$watch('transactions', function(){
      localStorageService.set('transactions', $scope.transactions);
    }, true);

    $scope.makeDeposit = function() {
      var transactionToStore = {'type':'DEPOSIT', 'amount':$scope.deposit.amount};
      $scope.transactions.push(transactionToStore);
      flash.setMessage({'type':'success','text':'Your account has been credited.'});
      $location.path('/customers/dashboard');
    };

    $scope.clear = function() {
      $scope.deposit = {};
    };
  }]);
