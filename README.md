# Mustard
Yet another Clojure static code analyze tool.

Mustard is a static Clojure code analyzer plugin for
[Leiningen](https://leiningen.org/). It's goal is for helping developers to
improve their code quality. Nothing too fancy or hard to use.

Mustard is currently under development and lacks most of its planned features.
Use it at your own risk.

Leiningen plugin can be found in Clojars:

![](https://clojars.org/lein-mustard/latest-version.svg)

[![Build Status](https://travis-ci.org/stormaaja/mustard.svg?branch=master)](https://travis-ci.org/stormaaja/mustard)

# Installing

Put `[lein-mustard "0.1.4"]` into the `:plugins` vector of a project or your
`:user` profile. For example `~/lein/profiles.clj` would look like:

```
{:user {:plugins [[lein-mustard "0.1.4"]]}}

```

# Usage

```bash
lein mustard
```

# Configure

## Parameters

| Key        | Description                     | Type              | Default       |
| ---------- |---------------------------------|-------------------|---------------|
| :src-paths | Overrides paths of source files | Vector of strings | :source-paths |

## Configuring

There is two ways to configure mustard and individual linters.

Modify `:mustard` in `project.clj` or `profile.clj`.

```
:mustard {:skip-tests? true
          :path "src"
          :unused-requires {:enable? false}}
```

# Linters

## Unused requires

Mustard seeks for unused requires. Because Mustard is static code analyze tool,
it will not find any unused :all requires or if there is redefined symbols.

Note that Mustard analyzes only first namespace declaring in file.

Mustard does not handle macros well at the moment.

Mustard does not handle reader conditionals (cljc-files) correctly at the
moment.

Mustard does not support ClojureScript files at the moment.

# License

Copyright © 2018 Matti Ahinko

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version, the same as Clojure.
