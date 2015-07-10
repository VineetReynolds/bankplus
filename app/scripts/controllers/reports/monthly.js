'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ReportsMonthlyCtrl
 * @description
 * # ReportsMonthlyCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ReportsMonthlyCtrl', ['$scope', 'monthlyStatementResource', function ($scope, monthlyStatementResource) {
    $scope.currentPage =1;
    $scope.statementLines = monthlyStatementResource.queryAll({'customerId':auth.customer.id});
  }]);
