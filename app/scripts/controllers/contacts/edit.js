'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsEditCtrl
 * @description
 * # ContactsEditCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsEditCtrl', ['$scope', '$location', '$routeParams', 'flash', 'contactResource', function ($scope, $location, $routeParams, flash, contactResource) {
    $scope.contact = contactResource.get({'customerId':auth.customer.id, 'contactId':$routeParams.contactId}, function() {
      $scope.originalContact = $scope.contact;
    });

    $scope.modifyContact = function() {
      $scope.contact.update(function () {
        flash.setMessage({'type':'success','text':'The contact was modified successfully.'});
        $location.path('/customers/dashboard');
      });
    };

    $scope.reset = function() {
      $scope.contact = $scope.originalContact;
    };
  }]);
