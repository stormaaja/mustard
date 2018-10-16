# mustard
Yet another Clojure static code analyze tool.

Mustard is a static Clojure code analyzer plugin for
[Leiningen](https://leiningen.org/). It's goal is for helping developers to
improve their code quality. Nothing too fancy or hard to use.

Mustard is currently under development and lacks most of its planned features.

# Installing

Put ![](https://clojars.org/mustard/latest-version.svg)
into the `:plugins` vector of a project or your `:user` profile.

# Usage

```bash
lein mustard
```

# Configure

## Parameters

| Key           | Description          | Default       |
| ------------- |----------------------|---------------|
| :path         | Path of source files | :source-paths |

## Configuring

There is two ways to configure mustard and individual linters.

Modify `:mustard` in `project.clj` or `profile.clj`.

```
:mustard {:skip-tests? true
          :path "src"
          :unused-requires {:enable? false}}
```

Or give configuration hash-map as command line arguments:

```bash
lein mustard "{:skip-tests? true :unused-requires {:enable? false}}"
```

# Linters

## Unused requires

Mustard seeks for unused requires. Because Mustard is static code analyze tool,
it will not find any unused :all requires or if there is redefined symbols.

Note that Mustard analyzes only first namespace declaring in file.

# License

Distributed under the Eclipse Public License, the same as Clojure.
