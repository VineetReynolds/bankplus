'use strict';

describe('Controller: ContactsViewCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var ContactsViewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ContactsViewCtrl = $controller('ContactsViewCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
