'use strict';

angular.module('bankPlusApp').factory('yearlyStatementResource', function($resource){
    var resource = $resource('/bankplus/rest/customers/:customerId/reports/yearly',null,{'queryAll':{method:'GET',isArray:true}});
    return resource;
});
