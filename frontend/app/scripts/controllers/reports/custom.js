'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:ReportsCustomCtrl
 * @description
 * # ReportsCustomCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('ReportsCustomCtrl', ['$scope', '$location', 'flash', 'customStatementResource', function ($scope, $location, flash, customStatementResource) {
    $scope.currentPage = 1;
    $scope.statementLines = [];

    $scope.generateReport = function() {
      if($scope.fromDate && $scope.toDate && ($scope.fromDate > $scope.toDate)) {
        flash.setMessage({'type':'danger','text':'From date cannot be later than To date.'}, true);
        return;
      }
      $scope.statementLines = customStatementResource.queryAll({'customerId':auth.customer.id, 'fromDate': $scope.fromDate, 'toDate': $scope.toDate});
    };

    $scope.open = function($event, fromOrTo) {
      $event.preventDefault();
      $event.stopPropagation();

      if(fromOrTo == 'from') {
        $scope.fromOpened = true;
      } else {
        $scope.toOpened = true;
      }

    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };

  }]);
