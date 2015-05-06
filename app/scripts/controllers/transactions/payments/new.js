'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsPaymentsNewCtrl
 * @description
 * # TransactionsPaymentsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsPaymentsNewCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
    var transactionsInStore = localStorageService.get('transactions');
    var contactsInStore = localStorageService.get('contacts');

    $scope.transactions = transactionsInStore || [];
    $scope.contactsInStore = contactsInStore || [];

    $scope.$watch('transactions', function(){
      localStorageService.set('transactions', $scope.transactions);
    }, true);

    $scope.makePayment = function() {
      var transactionToStore = {'type':'PAYMENT', 'payee':$scope.payment.contact, 'amount':$scope.payment.amount};
      $scope.transactions.push(transactionToStore);
      flash.setMessage({'type':'success','text':'Your account has been debited.'});
      $location.path('/customers/dashboard');
    };

    $scope.clear = function() {
      $scope.payment = {};
    };
  }]);
