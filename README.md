# poggio 

A puzzle game for exploring functional programming in a 3d environment.

## Build

Download the newest JMonkeyEngine libraries from [the JMonkeyEngine nightly builds](http://nightly.jmonkeyengine.org). Preferably, download the release from April 26, 2013 and create a local maven repository for it (the project.clj is set up to look at private_maven).

Alternatively, you can uncomment the older snapshots of the JMonkeyEngine libraries in project.clj. However, be aware that it may require some tweaks to the code and is not 100% compatible with certain Mac computers.

## Usage

The actual game can be played by running:

lein run -m poggio.core

You can access the level-editor by running:

lein run -m tools.level-editor.core

The documentation is light for the level-editor (and will hopefully be improved), but it's a pretty straightforward interface.

## License

Copyright (C) 2013

Distributed under the MIT License.
