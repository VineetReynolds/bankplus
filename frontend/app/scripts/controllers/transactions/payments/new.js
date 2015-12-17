'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:TransactionsPaymentsNewCtrl
 * @description
 * # TransactionsPaymentsNewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('TransactionsPaymentsNewCtrl', ['$scope', '$location', 'flash', 'contactResource', 'paymentResource', function ($scope, $location, flash, contactResource, paymentResource) {
    $scope.contactsInStore = contactResource.queryAll({'customerId':auth.customer.id});

    $scope.makePayment = function() {
      var transactionToStore = {'payeeId':$scope.payment.contact, 'amount':$scope.payment.amount};
      var successCallback = function(data,responseHeaders){
        flash.setMessage({'type':'success','text':'Your account has been debited.'});
        $location.path('/customers/dashboard');
      };
      var errorCallback = function(httpResponse) {
        flash.setMessage({'type':'danger','text':httpResponse.data.message}, true);
        $scope.displayError = true;
      };
      paymentResource.save({'customerId':auth.customer.id}, transactionToStore, successCallback, errorCallback);
    };

    $scope.clear = function() {
      $scope.payment = {};
    };
  }]);
