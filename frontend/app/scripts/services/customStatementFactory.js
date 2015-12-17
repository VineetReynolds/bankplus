'use strict';

angular.module('bankPlusApp').factory('customStatementResource', function($resource){
    var resource = $resource('/bankplus-reporting/rest/:customerId/reports/',null,{'queryAll':{method:'GET',isArray:true}});
    return resource;
});
