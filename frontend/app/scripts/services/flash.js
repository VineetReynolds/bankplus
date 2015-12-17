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
        return currentMessage;
      },
      setMessage: function(message, pop) {
        messages.push(message);
        if(pop) {
          currentMessage = messages.shift() || {};
        }
      }
    };
  }]);
