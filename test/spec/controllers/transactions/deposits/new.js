'use strict';

describe('Controller: TransactionsDepositsNewCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var TransactionsDepositsNewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TransactionsDepositsNewCtrl = $controller('TransactionsDepositsNewCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
