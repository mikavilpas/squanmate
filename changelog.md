# Changelog

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
