'use strict';

angular.module('bankPlusApp').factory('paymentResource', function($resource){
    var resource = $resource('/bankplus/rest/customers/:customerId/payments/:paymentId',{paymentId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false}});
    return resource;
});
