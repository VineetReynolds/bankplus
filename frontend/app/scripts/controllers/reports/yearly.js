'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ReportsYearlyCtrl
 * @description
 * # ReportsYearlyCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ReportsYearlyCtrl', ['$scope', 'yearlyStatementResource', function ($scope, yearlyStatementResource) {
    $scope.currentPage =1;
    $scope.statementLines = yearlyStatementResource.queryAll({'customerId':auth.customer.id});
  }]);
