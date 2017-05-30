#!/usr/bin/sh -e

git clone https://github.com/sp3ctum/cubeshape.git
cd cubeshape

# all deps should be present by default

# this prevents the installation of rmagick, maybe its version is too old?
rm Gemfile.lock

bundle install
