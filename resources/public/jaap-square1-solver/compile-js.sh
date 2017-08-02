#!/usr/bin/env bash
# This compiles this C++ application to javascript using the cheerp compiler. It
# needs to be installed separately.
/opt/cheerp/bin/clang++ -target cheerp *.cpp -o solver.js -g -cheerp-sourcemap=solver.js.map
