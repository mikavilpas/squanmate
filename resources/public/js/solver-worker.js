"use strict";

importScripts("web-worker-proxies.js", "../jaap-square1-solver/solver.js");

this.proxy = {
  solve: function(startState) {
    var solver = new Square1Solver();
    return solver.solve(startState);
  }
};
