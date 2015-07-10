'use strict';

angular.module('bankPlusApp').factory('customStatementResource', function($resource){
    var resource = $resource('/bankplus/rest/customers/:customerId/reports/',null,{'queryAll':{method:'GET',isArray:true}});
    return resource;
});
