'use strict';

angular.module('bankPlusApp').factory('customerResource', function($resource){
    var resource = $resource('/bankplus/rest/customers/:customerId',{customerId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});
