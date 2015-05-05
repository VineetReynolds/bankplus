'use strict';

/**
 * @ngdoc overview
 * @name bankPlusApp
 * @description
 * # bankPlusApp
 *
 * Main module of the application.
 */
angular
  .module('bankPlusApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'LocalStorageModule'
  ])
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl'
      })
      .when('/customers/dashboard', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashboardCtrl'
      })
      .when('/contacts/new', {
        templateUrl: 'views/contacts/new.html',
        controller: 'ContactsRegisterCtrl'
      })
      .when('/contacts/view', {
        templateUrl: 'views/contacts/view.html',
        controller: 'ContactsViewCtrl'
      })
      .when('/transactions/deposits/new', {
        templateUrl: 'views/transactions/deposits/new.html',
        controller: 'TransactionsDepositsNewCtrl'
      })
      .when('/transactions/withdrawals/new', {
        templateUrl: 'views/transactions/withdrawals/new.html',
        controller: 'TransactionsWithdrawalsNewCtrl'
      })
      .when('/transactions/payments/new', {
        templateUrl: 'views/transactions/payments/new.html',
        controller: 'TransactionsPaymentsNewCtrl'
      })
      .when('/reports/monthly', {
        templateUrl: 'views/reports/monthly.html',
        controller: 'ReportsMonthlyCtrl'
      })
      .when('/reports/yearly', {
        templateUrl: 'views/reports/yearly.html',
        controller: 'ReportsYearlyCtrl'
      })
      .when('/reports/custom', {
        templateUrl: 'views/reports/custom.html',
        controller: 'ReportsCustomCtrl'
      })
      .when('/contacts/edit/:contactId', {
        templateUrl: 'views/contacts/edit.html',
        controller: 'ContactsEditCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  }])
  .config(['localStorageServiceProvider', function(localStorageServiceProvider){
    localStorageServiceProvider.setPrefix('bankplus');
  }]);
