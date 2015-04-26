'use strict';

describe('Controller: FlashCtrl', function () {

  // load the controller's module
  beforeEach(module('bankPlusApp'));

  var FlashCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    FlashCtrl = $controller('FlashCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
