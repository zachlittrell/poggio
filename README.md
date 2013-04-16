# poggio 

A puzzle game (very much early in progress) for exploring functional
programming in a 3d environment.

## Usage

The basic gist of the game can be seen by running:

lein run -m tools.level-viewer.core test/poggio/test/poggio/levels/sliding\_doors\_level.clj

By right clicking on the cannon, you can access its internal function, which
takes a list of colors. By hitting compute, the cannon will spit out globules
for each color in the list.

Some functions to try:

cons red ()

triple red green blue

flatten repeat triple red green blue

It's still chock-full of bugs.

## License

Copyright (C) 2013

Distributed under the MIT License.
