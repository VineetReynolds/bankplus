'use strict';
var module = angular.module('bankPlusApp', []);

var auth = {};
var logout = function(){
  console.log('*** LOGOUT');
  auth.loggedIn = false;
  auth.authz = null;
  window.location = auth.logoutUrl;
};

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
    'angularUtils.directives.dirPagination',
    'ui.bootstrap'
  ])
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashboardCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
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
  .factory('Auth', function () {
    return auth;
  })
  .factory('authInterceptor', function ($q, Auth) {
    return {
      'request': function (config) {
        var deferred = $q.defer();
        if (Auth.authz && Auth.authz.token) {
          Auth.authz.updateToken(5).success(function () {
            config.headers = config.headers || {};
            config.headers.Authorization = 'Bearer ' + Auth.authz.token;

            deferred.resolve(config);
          }).error(function () {
            Auth.authz.clearToken();
            deferred.reject('Failed to refresh token');
          });
        }
        return deferred.promise;
      }
    };
  })
  .factory('errorInterceptor', function ($q) {
    return {
      'responseError': function (rejection) {
        if (rejection.status == 401) {
          console.log('session timeout?');
          logout();
        } else if (rejection.status == 403) {
          console.log("Forbidden");
        } else if (rejection.status == 404) {
          console.log("Not found");
        } else if (rejection.status) {
          if (rejection.data && rejection.data.errorMessage) {
            console.log(rejection.data.errorMessage);
          } else {
            console.log("An unexpected server error has occurred");
          }
        }
        return $q.reject(rejection);
      }
    };
  })
  .config(function ($httpProvider) {
    $httpProvider.interceptors.push('errorInterceptor');
    $httpProvider.interceptors.push('authInterceptor');
  });

angular.element(document).ready(function ($http) {
  var keycloakAuth = new Keycloak('keycloak.json');
  auth.loggedIn = false;

  keycloakAuth.init({ onLoad: 'login-required' }).success(function () {
    auth.loggedIn = true;
    auth.authz = keycloakAuth;
    auth.logoutUrl = keycloakAuth.authServerUrl + "/realms/BankPlus/tokens/logout?redirect_uri=http://localhost/";

    var initInjector = angular.injector(["ng"]);
    var $http = initInjector.get("$http");
    var $q = initInjector.get("$q");

    updateToken().then(fetchCustomer().finally(bootstrapApplication));

    function updateToken() {
      return $q(function(resolve, reject){
        keycloakAuth.updateToken(10)
          .success(function() {
            resolve();
          })
          .error(function() {
            reject('Failed to update the token');
          });
      });
    }

    function fetchCustomer() {
      var req = {
        method: 'GET',
        url: "./bankplus-customers/rest/customers?email=" + auth.authz.idTokenParsed.email,
        headers: {
          'Authorization': 'Bearer ' + auth.authz.token,
          'Accept': 'application/json'
        },
        timeout: 10000
      };

      return $q(function(resolve, reject){
        $http(req).then(function(response) {
          if (response.data.length == 0) {
            // No matching customer record was found in BankPlus
            console.log("Failed to locate customer in the backend.");
            logout();
            reject();
            return;
          } else {
            auth.customer = response.data[0];
          }
          resolve();
        }, function(errorResponse) {
          console.log("Error verifying customer data record..");
          logout();
          reject();
        });
      });
    }

    function bootstrapApplication() {
      angular.bootstrap(document, ['bankPlusApp']);
    }
  }).error(function () {
    window.location.reload();
  });

});
