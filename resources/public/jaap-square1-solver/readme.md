SQUARE-1 SOLVER version 3.0
~~~~~~~~~~~~~~~
by Jaap Scherphuis, jaap@org2.com, copyright 2001.

This program implements a search algorithm to find move sequences to
solve a given position in the Square 1 puzzle.



The Square 1 Puzzle
~~~~~~~~~~~~~~~~~~~

This puzzle is a cube consisting of three layers. The top and bottom layers
are cut like a pie in 8 pieces; 4 edge pieces and 4 corner pieces, 30 and 60
degrees wide respectively. The top and bottom layers can rotate. The middle
layer is cut in only two halves along one of the lines of the other layers.
If there are no corner pieces in the way, you can twist half the cube 180
degrees so that pieces from the top and bottom layers mingle.

The puzzle is unique in that the two types of pieces intermingle. The edge
and corner pieces can freely move between the two outer layers. Of course,
the puzzle will not necessarily be a cube shape when the pieces are mixed.
The puzzle has six colours, each face has a single colour similar to the
Rubik's cube. The aim is of course to return a mixed puzzle back to its
original solved position.

The number of positions:
There are three categories of puzzle shapes.
  a. Both layers have 4 edges and 4 corners each.
  b. One layer has 3 corners, 6 edges, the other 5 corners 2 edges.
  c. One layer has 2 corners, 8 edges, the other 6 corners and no edges.

There are 1, 3, 10, 10 and 5 layer shapes with 6, 5, 4, 3 and 2 corners.
This means there are 5*1+10*3+10*10+3*10+1*5 = 170 shapes for the top and
bottom layers. The middle layer has two shapes (half of it is assumed to be
fixed). This means that there seem to be 170*2*8!*8! = 552,738,816,000
positions if we disregard rotations of the layers. Some layer shapes however
have symmetry, and these have been counted too many times this way.


To take account of the symmetries we can simply count the number of layer
shapes differently. Instead of the numbers 1, 3, 10, 10, 5 we use the
numbers 2, 36, 105, 112, 54, which are the number of shapes if we consider
rotations different (e.g. a square counts as 3 because it has three possible
orientations). By the same method as before we then get 19305*2*8!*8!
or 627,768,369,664,000 positions.

To exclude layer rotations, divide by 12^2 to get a total of 435,891,456,000
distinct positions.



The Search Algorithm
~~~~~~~~~~~~~~~~~~~~

The search algorithm is very similar in design to the Kociemba
algorithm for solving the Rubik's cube, as it solves it in two stages
and uses tables to prune the search tree.

During the first stage of the search a position is found in which the
top and bottom layers are square and where the pieces lie in an even
permutation. The second stage will then solve it with moves that keep
the top and bottom layers square.

The first stage uses a single look-up table, that holds the minimal number
of moves needed to complete this stage from the current position.

The second stage uses in effect two look-up tables, one for the edges and
one for the corners, and the number of moves needed to solve them is given.
In reality the two tables are identical, because the action of the / move
is the same on corners as on edges.

The search is limited by a maximum depth. This is 99 by default, but can
be set to a lower value by the user. Whenever a solution is found the
depth is set to (one less than) the length of that solution, so that only
solutions of equal (or shorter) length will be searched for from that point on.

The two stage search is performed by first searching for solutions to the
first phase, and for each of those performing a second phase search. The
shortest first phase solution does not necessarily mean the shortest overall
solution. It is usually the case that some longer first phase solution has
a much shorter second phase part, leading to a shorter solution overall.
Therefore once all first phase solutions of a particular length have been
checked, longer ones are tried. Eventually the first phase search will
reach the maximum depth and there will be no second phase at all. This
will take far too long in most cases, so you will probably abort the
program after some reasonable solutions have been found.



Notation
~~~~~~~~

First we need a notation for the moves used on the puzzle. Hold the
puzzle so that the yellow middle layer piece is on the left hand side
with its 'Square-1' inscription the right way up. Denote a 180 degree
twist of the right hand side of the puzzle by a / sign (a slash). This
kind of move will be called simply a 'twist'. Turns of the top and bottom
layers are denoted by a pair of numbers (n,m). These numbers are the
multiple of 30 degrees clockwise that the top/bottom layers are to turn
respectively. Thus (3,0) means turn only the top layer clockwise 90
degrees, and (0,-1) means turn only the bottom layer 30 degrees
anti-clockwise (i.e. one edge along). 

There are generally two ways to count the length of a sequence:
Twist Metric:  The length is simply the number of twists.
                 For example, / is one move, and /(6,6)/(-1,1) is 2 moves.
Turn Metric:   The length is the number of (non-zero) turns and twists.
                 For example, (3,0) is one move, as is /, and /(6,6)/(-1,1)
                 is 6 moves.
This version of the program can use either metric.

Note that there is an equivalence. The move sequence
  (a,b)/(c,d)/(e,f)
has the same effect as the sequence
  (6+a,6+b)/(d,c)/(6+e,6+f)
The program can only make use of this if the twist metric is used. In this
case the bottom layer is never turned 6 or more, except possibly just before
of just after the final twist. 

We also need a notation for each position of the puzzle. All the corner
pieces are denoted by letters, the edges by digits. On the solved puzzle,
the top layer pieces are A1B2C3D4 reading clockwise from the front left
corner, and the bottom pieces are 5E6F7G8H clockwise from the front edge.
The solved position is then denoted by A1B2C3D45E6F7G8H. Any mixed
position can be similarly coded, simply listing the pieces in the top
layer clockwise from cut in the front of the middle layer, followed by the
bottom layer pieces in a similar manner. If shape of the middle layer
should be considered, it can denoted by appending a - or a / to indicate
whether it is square or kite-shaped respectively.

The standard colour scheme of the puzzle has the following colours:
Left:   Yellow
Front:  Orange
Right:  Blue
Back:   Red
Top:    White
Bottom: Green

For easy reference, here are the letters/numbers used in the position
notation above:

Letter   Colours
  A        WYO
  B        WRY
  C        WBR
  D        WOB
  E        GOB
  F        GBR
  G        GRY
  H        GYO

Number   Colours
  1        WY
  2        WR
  3        WB
  4        WO
  5        GO
  6        GB
  7        GR
  8        GY


Using the program
~~~~~~~~~~~~~~~~~

Once you understand the notation, the program is fairly simple to use. Run
the program from the DOS command line as follows:
   SQUARE1 <position> [outputfile]
where the output-file is optional. To abort the program, press Ctrl-C or
Ctrl-Break.

The position to be solved can be specified in two ways. Either by a sequence of
moves, e.g. (1,0)/(3,0)/(6,-3)/(2,3), or by a position, e.g. A2B3C1D45E6F7G8H,
as explained above. Note that there should be no spaces in a list of moves.

Some examples will be given further on in this text.

There are some optional command line switches you can use:
  -t    Use the Twist metric. By default, the Turn metric is used.
  -m    Ignore middle layer. This is only relevant if you give it a list of moves.
  -a    Search for all best sequences, not just one. If the best sequence so
           far is of length 8, then without this switch only sequences of
           length 7 or less will be searched. With this switch, further length
           8 sequences are still found.
  -lxx  Sets the maximum length to xx. For example, -l20 will limit the search
           to sequences of at most 20 moves. In some cases this reduces the time
           it takes (especially if there is a long phase 2 search). Note that if
           a move list is given, then the maximum depth is automatically set to
           the length of that list. If you want to use this switch in such a
           case, make sure it comes after the move list.


During the search, the screen will show a line such as:
   Len1: 13  Nodes1:2800000  Len2: 5  Nodes2:312991

This shows the lengths of the two search phases, and also how many positions
it has looked at in each phase.

When a solution is found it is displayed too, and written to the output file
if one was specified. The length of the solutions is given as [a|b], where a
is the length in Twist metric (in other words a is simply the number of / moves)
and b is the length in Turn metric. For example
(1,0)/(3,0)/(6,-3)/(2,3)   [3|9]



Example 1:
   SQUARE1 A2B3C1D45E6F7G8H-
This searches for a sequence of moves that cycles 3 of the top layer edges
(edges 1, 2  and 3) and puts any solution it finds on the screen. No limit is
given for the search so it takes a while before the first solution of
length [8|21] is found. It will find a shorter solution fairly quickly so
this limit is soon restricted further. In fact, it will find an [8|16] move
solution nearly immediately afterwards. Note that only sequences with an even
number of twists will be considered because the shape of the middle layer
has been specified as being square at the start.


Example 2:
   SQUARE1 A2B3C1D45E6F7G8H
As example 1, but now the middle layer is ignored. This time it first finds
a [7|19] solution, but then settles for the same [8|16] solution as before.
If we had used 
   SQUARE1 A2B3C1D45E6F7G8H/
then the search would concentrate specifically on sequences an odd number
of twists only, and it finds the [7|19] solution, and then the shorter
one of length [9|18].


Example 3:
   SQUARE1 A2B3C1D45E6F7G8H tricycle.txt
As example 2, but the sequences are also stored in an ASCII file named
tricycle.txt.


Example 4:
   SQUARE1 -a -l16 A2B3C1D45E6F7G8H tricycle.txt
As example 2, but now all sequences of length 16 (and less) are found, and
saved in the file tricycle.txt. The search starts at length 16, and it will
find all such sequences. However if at any time it finds one of length 15,
then no others of length 16 will be shown, only all further ones of length 15.



Example 5:
   SQUARE1 (4,0)/(5,-4)/(1,-5)/(0,3)/(0,-3)/(-1,-4)/(-5,4)/(5,0)
This is one of the sequences that was produced in example 2. It will try to
find the shortest sequence that has the same effect as the given sequence.
It will only find solutions with an odd number of twists, because the middle
layer is not ignored.


Example 6:
   SQUARE1 -m (4,0)/(5,-4)/(1,-5)/(0,3)/(0,-3)/(-1,-4)/(-5,4)/(5,0)
As above, but now it finds solutions with any number of twists because the
middle layer is ignored.


Example 7:
   SQUARE1 -m -a (4,0)/(5,-4)/(1,-5)/(0,3)/(0,-3)/(-1,-4)/(-5,4)/(5,0) -l16
As above, but it finds only solutions with 16 moves or less (and any
number of twists), and will find all those of minimal length.



Results
~~~~~~~
Most of the results that I have found using this program are on my web site.
I suspect that God's algorithm (the shortest possible way of solving any 
position) uses at most about 12 twists or at most 30 moves, but clearly
this cannot be proved conclusively with a program such as this.


Revision History
~~~~~~~~~~~~~~~~
Jan 2000:  Version 1.00
              This was written in C, and counted only the number of twists.
              It was somewhat slow, because the code was not well designed.
May 2001:  Version 2.00
              This was written in C++. Now all layer turns are counted as
              moves as well. It is much quicker through the use of
              transition tables, but could not make use of an equivalence
              of moves. The code is also much cleaner than the first version
              because of Object Orientation.
           Version 2.01
              Two bugs fixed: Some bogus solutions occurred, and some solutions
              were not found.
May 2001:  Version 3.00
              Allowed the choice of metric.

Future Improvements:
- Embed it in a windows interface


Jaap's Puzzle page:  http://www.org2.com/jaap/puzzles

----------------------------------------------------------------------------
This program was written by Jaap Scherphuis, copyright May 2001.
It may not be sold. It may be freely distributed provided that this
documentation is provided in some form without changes to the text.

The program can be downloaded from Jaap's Puzzle Page:
http://www.org2.com/jaap/puzzles
Source code is available on request from the author:
mailto:jaap@org2.com
