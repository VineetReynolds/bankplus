'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ContactsViewCtrl
 * @description
 * # ContactsViewCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ContactsViewCtrl', ['$scope','contactResource', function ($scope, contactResource) {
    $scope.contacts = contactResource.queryAll({'customerId':auth.customer.id});

    $scope.performSearch = function() {
      $scope.searchResults = $scope.contacts;
    };

    $scope.performSearch();
  }]);
