'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ReportsCustomCtrl
 * @description
 * # ReportsCustomCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ReportsCustomCtrl', ['$scope', 'customStatementResource', function ($scope, customStatementResource) {
    $scope.statementLines = customStatementResource.queryAll({'customerId':auth.customer.id, 'fromDate': $scope.fromDate, 'toDate': $scope.toDate});
  }]);
