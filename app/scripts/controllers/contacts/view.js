'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsViewCtrl
 * @description
 * # ContactsViewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsViewCtrl', ['$scope','localStorageService', function ($scope, localStorageService) {
    var contactsStore = localStorageService.get('contacts');

    $scope.contacts = contactsStore || [];

    $scope.performSearch = function() {
      $scope.searchResults = $scope.contacts;
    };

    $scope.performSearch();
  }]);
