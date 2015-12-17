'use strict';

describe('Controller: ContactsEditCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var ContactsEditCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ContactsEditCtrl = $controller('ContactsEditCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
