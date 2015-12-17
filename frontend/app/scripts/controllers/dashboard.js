'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('DashboardCtrl', ['$scope', 'customerResource', function ($scope, customerResource) {
    customerResource.query({'customerId':auth.customer.id}, function(response){
      $scope.account = response.account;
    });
  }]);
