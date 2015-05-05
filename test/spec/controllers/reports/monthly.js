'use strict';

describe('Controller: ReportsMonthlyCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var ReportsMonthlyCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ReportsMonthlyCtrl = $controller('ReportsMonthlyCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
