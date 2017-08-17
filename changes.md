# Changelog

## 3.4.1
- shape-visualizer: fix errors in switching layers in some cases

## 3.4
- shape-visualizer: add button to switch current layers "upside down". This will
  convert the initial rotation and algorithm to match as well.

## 3.3
- fix major crash in main ui (oops!), after which the app was unresponsive

## 3.2
- shape-visualizer: fix visualization getting stuck when alg is changed

## 3.1
- trainer: add filters and controls for selecting the subset of shapes to get
  scrambles in. Previously the shapes were all in a big, confusing list.

## 3.0
- fix not being able to navigate to other pages after clicking/opening a
  shape-visualizer "link to this visualization" link
- change version scheme to semantic versioning. Should have been semver from the
  start :)

## 2017-08-12
- shape-visualizer: fix showing parity count for algs that don't end in
  cubeshape

## 2017-08-10
- trainer: remove previous scramble sequence when a new scramble is being
  generated, to make it clear it's not been calculated yet.
- "all shapes" page: fix reporting 11 "4 corners, 4 edges" shapes while there
  actually are 10
- "all shapes" page: add nicer looking formatting

## 2017-08-09
- new feature: cubeshape parity trainer! See the readme for more info.
- shape-visualizer bugfix: `BREAKING`: Fix being able to rotate the bottom layer
  into impossible positions. The pieces were actually aligned at the slice point
  at back of the puzzle, while they should have been aligned at the front. All
  shape-visualizer algs need to be adjusted with an additional 0,6 initial
  rotation to account for this bug.

## 2017-07-31
- shape-visualizer: show Cale Schoon method parity count for the current count
  position and algorithm

## 2017-07-24-bugfix
- shape-visualizer: fix generating invalid shape visualization urls with 0
  initial rotation (or algorithm)

## 2017-07-24
- shape-visualizer: add "link to this visualization" button. Now it's possible
  to share and save specific visualizations easily.
