'use strict';

describe('Controller: TransactionsPaymentsNewCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var TransactionsPaymentsNewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TransactionsPaymentsNewCtrl = $controller('TransactionsPaymentsNewCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
