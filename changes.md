# Changelog

## upcoming
- bugfix: color chooser component was not always visible on mobile devices
  because it was drawn outside the screen boundaries

## 6.2.1
- use local styles for "select shape" components. They used to use styles
  provided externally, but the provider is currently down.

## 6.2
- new feature: choose custom colors for any side of your puzzle. The colors will
  be used anywhere a puzzle is drawn.

## 6.1
- new feature: both trainers' scrambles now highlight the part of the scramble
  that is out of cubeshape (square square). The idea is that it is easier to
  follow along.

## 6.0
- `BREAKING`: puzzle colors are now defined on a dedicated Settings page.
  Previously the colors had to be defined in three separate places (cubeshape
  trainer, algorithm trainer and scramble inspector).
- bugfix: cubeshape trainer's "choose none" button worked only when all of the
  visible shape cases were selected

## 5.12
- change: a scramble is no longer generated automatically when Squanmate is
  opened
- new feature: scramble inspector's parity analysis box now shows the sequence
  of colors in pieces that make up the parity
- new feature: that same box now explains why the "Swap top and bottom colors"
  color selection seemingly reports odd color sequences as even, and vice versa.
  I believe this is not strictly the case (ask me separately if you'd like to
  know), but having a message like this should help people understand how to use
  the feature quickly with minimal confusion

## 5.11
- new feature: algorithm trainer's algorithm sets now display the count and
  percentage of selected cases

## 5.10
- new feature: algorithm trainer now allows selecting even or odd parity cases
  only.

## 5.9.1
- bugfix: it was impossible to open setting panels by clicking on them, except
  by clicking exactly on the text. Now they can be opened from any position.
  This fix isn't perfect - there are some areas above and below the level of the
  text that still don't work.
- bugfix: force all cubeshape parity trainer scrambles to have a sliceable
  starting position. This is something that I understand is not in the actual
  regulations, but it seems in practice all scrambles work this way already.

## 5.9
- new feature: cubeshape trainer and algorithm trainer now remember their
  settings across page reloads / closing the browser etc.

## 5.8
- new feature: parity game: use the left and right arrow to answer which parity
  is shown.
- bugfix: parity game sometimes gave the same case again after a correct answer

## 5.7
- new feature: cubeshape trainer now allows deselecting the current case and
  generating a new one.

## 5.6
- new feature: "Repeat case with same/opposite parity" in cubeshape trainer. See
  the readme for more info.
- bugfix: fix "Swap top and bottom colors" resulting in non-standard color
  scheme

## 5.5
- new feature: both trainers now allow choosing whether the middle layer should
  be flipped in the scrambles. The default is to have a random middle layer
  orientation in both trainers.
- new feature: algorithm trainer now generates cases in random orientations.
  Previously the cases were generated only at the position the algorithm (that
  solves the case) would start at. 

## 5.4
- new feature: add PLL algorithms to the algorithm trainer

## 5.3
- new feature: algorithm trainer. Support for all edge permutation (EP) cases.
  See the readme for more info.

## 5.2
- new feature: add a "reapeat scramble button". Let's say you have the randomly
  selected case of shield kite. Clicking this button will generate another
  scramble with the shield and kite shapes.

## 5.1.1
- bugfix/feature: google analytics now tracks all page loads and "new scramble"
  button usages

## 5.1
- trainer: now there is an "Inspect" button that lets you view the generated
  scramble in the Scramble Inspector. This can be useful when you want to double
  check your parity count.
- bugfix: fix scramble inspector crashing on a scramble that left the puzzle in
  a non-sliceable position.

## 5.0
- new feature: parity analysis (part of Scramble Inspector). See the readme.
- bugfix: Shape visualizer was unusable, it crashed when a new shape was loaded.
  Now it works again.
- parity count positions: `BREAKING`: fix count position being off by 6. Now the
  count starts at the shown position and moves clockwise.

## 4.6
- new feature: scramble inspector. See the readme for more info.

## 4.5
- new feature: parity game. See the readme for more info.

## 4.4
- new feature: parity count positions. See the readme for more info.

## 4.3.2
- color settings: make y2 act like actual y2 move. It used to only swap the
  front and back colors.

## 4.3.1
- fix puzzle previews not rendering initially on Firefox. See
  https://github.com/sp3ctum/squanmate/issues/23 for the description.

## 4.3
- new feature: choose the colors of your specific Square-1 for the trainer.

## 4.2
- add google analytics (I want to see how much this app is used)

## 4.1
- new feature: Cubeshape algorithm importer! See the readme for more info.

## 4.0
- shape-visualizer: replace "initial rotation" textbox with layer rotation
  buttons. The rotation buttons feel a lot more intuitive and fun to use. For
  now only rotations that leave the layer in a sliceable position are allowed.
- shape-visualizer: add a "clear" button
- shape-visualizer: `BREAKING`: some shapes' default positions have changed. Any
  algorithms that use these shapes will have to do the following adjustments
  (see these changes):
  - https://github.com/sp3ctum/squanmate/commit/ab4db05
  - https://github.com/sp3ctum/squanmate/commit/24d62b4
  - if you have any problem, open an issue that includes a link to your faulty
    alg, and I can help you correct it.

## 3.5
- shape-visualizer: only allow selecting layers that are possible to construct
  based on the other selected layer. If no other layer is selected, any layer
  can be selected. If e.g. "star" is selected as the other layer, only the 5
  cases that are possible are available.

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
