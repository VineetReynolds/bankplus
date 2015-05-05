'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsRegisterCtrl
 * @description
 * # ContactsRegisterCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsRegisterCtrl', ['$scope', '$location', 'flash', 'localStorageService', function ($scope, $location, flash, localStorageService) {
    var contactsStore = localStorageService.get('contacts');
    var currentId = parseInt(localStorageService.get('contactsCounter')) || 0;

    $scope.contacts = contactsStore || [];

    $scope.$watch('contacts', function(){
      localStorageService.set('contacts', $scope.contacts);
    }, true);

    $scope.registerContact = function() {
      currentId++;
      var contactToStore = {'id':currentId, 'name':$scope.contact.name, 'iban':$scope.contact.iban};
      localStorageService.set('contactsCounter', currentId);
      $scope.contacts.push(contactToStore);
      flash.setMessage({'type':'success','text':'The contact was added successfully.'});
      $location.path('/customers/dashboard');
    };

    $scope.clearUser = function() {
      $scope.contact = {};
    };
  }]);
