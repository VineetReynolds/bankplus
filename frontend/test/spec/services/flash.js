'use strict';

describe('Service: flash', function () {

  // load the service's module
  beforeEach(module('bankPlusApp'));

  // instantiate service
  var flash;
  beforeEach(inject(function (_flash_) {
    flash = _flash_;
  }));

  it('should do something', function () {
    expect(!!flash).toBe(true);
  });

});
