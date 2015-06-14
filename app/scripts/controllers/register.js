'use strict';

/**
 * @ngdoc function
 * @name bankPlusApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the bankPlusApp
 */
angular.module('bankPlusApp')
  .controller('RegisterCtrl', ['$scope', '$location', 'flash', 'customerResource', function ($scope, $location, flash, customerResource) {
    $scope.registerCustomer = function() {
      var userToStore = {
        'fullName': auth.authz.idTokenParsed.name,
        'mailingAddress': $scope.user.mailingAddress,
        'emailAddress': auth.authz.idTokenParsed.email,
        'phoneNumber':  $scope.user.phoneNumber,
        'mobileNumber': $scope.user.mobileNumber};
      var successCallback = function(data,responseHeaders){
        auth.shouldRegisterUser = false;
        auth.customer = data;
        flash.setMessage({'type':'success','text':'You have been registered successfully.'});
        $location.path('/');
      };
      var errorCallback = function() {
        $scope.displayError = true;
      };
      customerResource.save(userToStore, successCallback, errorCallback);
    };

    $scope.clearUser = function() {
      $scope.user = {};
    };
  }]);
