'use strict';

angular.module('bankPlusApp').factory('contactResource', function($resource){
    var resource = $resource('/bankplus/rest/customers/:customerId/contacts/:contactId',{contactId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});
