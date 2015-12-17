'use strict';

angular.module('bankPlusApp').factory('yearlyStatementResource', function($resource){
    var resource = $resource('/bankplus-reporting/rest/:customerId/reports/yearly',null,{'queryAll':{method:'GET',isArray:true}});
    return resource;
});
