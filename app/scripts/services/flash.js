'use strict';

/**
 * @ngdoc service
 * @name bankPlusApp.flash
 * @description
 * # flash
 * Factory in the bankPlusApp.
 */
angular.module('bankPlusApp')
  .factory('flash', ['$rootScope', function ($rootScope) {
    var messages = [];
    var currentMessage = {};

    $rootScope.$on('$routeChangeSuccess', function() {
      currentMessage = messages.shift() || {};
    });

    return {
      getMessage: function () {
        console.log(currentMessage);
        return currentMessage;
      },
      setMessage: function(message) {
        messages.push(message);
      }
    };
  }]);
