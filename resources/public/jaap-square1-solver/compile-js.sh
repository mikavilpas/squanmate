#!/usr/bin/env bash
# This compiles this C++ application to javascript using the cheerp compiler. It
# needs to be installed separately.
/opt/cheerp/bin/clang++ -target cheerp webmain.cpp -o solver.js
