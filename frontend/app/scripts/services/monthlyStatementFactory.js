'use strict';

angular.module('bankPlusApp').factory('monthlyStatementResource', function($resource){
    var resource = $resource('/bankplus-reporting/rest/:customerId/reports/monthly',null,{'queryAll':{method:'GET',isArray:true}});
    return resource;
});
