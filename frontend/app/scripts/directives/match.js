'use strict';

/**
 * @ngdoc directive
 * @name bankPlusApp.directive:match
 * @description
 * # match
 */
angular.module('bankPlusApp')
  .directive('match', function () {
    return {
      require: 'ngModel',
      link: function postLink(scope, element, attrs, ctrl) {
        scope.$watch('[' + attrs.ngModel + ',' + attrs.match + ']', function(value){
          ctrl.$setValidity('match', value[0] === value[1]);
        }, true);
      }
    };
  });
