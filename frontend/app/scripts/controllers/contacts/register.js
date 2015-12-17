'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsRegisterCtrl
 * @description
 * # ContactsRegisterCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsRegisterCtrl', ['$scope', '$location', 'flash', 'contactResource', function ($scope, $location, flash, contactResource) {

    $scope.registerContact = function() {
      var contactToStore = {'fullName':$scope.contact.name, 'iban':$scope.contact.iban};
      var successCallback = function(data,responseHeaders){
        flash.setMessage({'type':'success','text':'The contact was added successfully.'});
        $location.path('/customers/dashboard');
      };
      var errorCallback = function() {
        $scope.displayError = true;
      };
      contactResource.save({'customerId':auth.customer.id}, contactToStore, successCallback, errorCallback);
    };

    $scope.clearUser = function() {
      $scope.contact = {};
    };
  }]);
