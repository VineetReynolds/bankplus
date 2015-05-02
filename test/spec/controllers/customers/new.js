'use strict';

describe('Controller: CustomersNewCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var CustomersNewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CustomersNewCtrl = $controller('CustomersNewCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
