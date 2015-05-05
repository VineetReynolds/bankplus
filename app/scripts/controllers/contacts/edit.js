'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsEditCtrl
 * @description
 * # ContactsEditCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsEditCtrl', ['$scope', '$location', '$routeParams', 'flash', 'localStorageService', function ($scope, $location, $routeParams, flash, localStorageService) {
    var contactsInStore = localStorageService.get('contacts');

    $scope.contacts = contactsInStore || [];

    var loadContact = function() {
      for(var ctr = 0; ctr < $scope.contacts.length; ctr++) {
        var contact = $scope.contacts[ctr];
        if(parseInt($routeParams.contactId) === contact.id) {
          $scope.contact = contact;
          $scope.originalContact = contact;
          return;
        }
      }
    }

    $scope.$watch('contacts', function(){
      localStorageService.set('contacts', $scope.contacts);
    }, true);

    $scope.modifyContact = function() {
      var contactToStore = {'id':$scope.contact.id, 'name':$scope.contact.name, 'iban':$scope.contact.iban};
      for(var ctr = 0; ctr < $scope.contacts.length; ctr++) {
        var contact = $scope.contacts[ctr];
        if(contactToStore.id === contact.id) {
          $scope.contacts[ctr] = contactToStore;
          break;
        }
      }
      flash.setMessage({'type':'success','text':'The contact was modified successfully.'});
      $location.path('/customers/dashboard');
    };

    $scope.reset = function() {
      $scope.contact = $scope.originalContact;
    };

    loadContact();
  }]);
