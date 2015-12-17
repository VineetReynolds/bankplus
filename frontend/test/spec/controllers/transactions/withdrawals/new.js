'use strict';

describe('Controller: TransactionsWithdrawalsNewCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var TransactionsWithdrawalsNewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TransactionsWithdrawalsNewCtrl = $controller('TransactionsWithdrawalsNewCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
