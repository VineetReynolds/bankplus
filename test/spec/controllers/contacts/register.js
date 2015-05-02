'use strict';

describe('Controller: ContactsRegisterCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var ContactsRegisterCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ContactsRegisterCtrl = $controller('ContactsRegisterCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
