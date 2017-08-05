"use strict";

importScripts("web-worker-proxies.js", "../jaap-square1-solver/solver.js");
var solver = new Square1Solver();

this.proxy = {
  solve: function(startState) {
    return solver.solve(startState);
  }
};
