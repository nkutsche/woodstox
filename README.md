# Overview

The gold standard Stax XML API implementation. Now at Github.

[FORKED VERSION] Plese note, that this is a forked version of 
[FasterXML/woodstox](https://github.com/FasterXML/woodstox).

# Get it!

## Maven

The most common way is to use Maven (or Ivy) to access it from Maven Central repository.
Coordinates for this are:

* Group id: `com.github.nkutsche`
* Artifact id: `woodstox-core`
* Latest published version: 5.1.0 (24-June-2017)
    * (NOTE! Version `5.0.0` was accidentally released as broken, not containing actual classes -- 5.0.1 is the first functioning 5.x version).

Note that Maven id has changed since Woodstox 4.x.

## Requirements

Woodstox 5 and above require Java 6 (JDK 1.6); as well as Stax API that is included in JDK.
The only other mandatory dependence is [Stax2 API](../../../stax2-api), extended API implemented by Woodstox
and some other Stax implementations (like [Aalto](../../../aalto-xml).

Optional dependency is [Multi-Schema Validator (MSV)](https://github.com/kohsuke/msv) that is needed if
using XML Schema or RelaxNG validation functionality

## License

Woodstox 5.x is licensed under [Apache 2](http://www.apache.org/licenses/LICENSE-2.0.txt) license.

## Documentation etc

* User mailing list for Qs: [woodstox-user](https://groups.google.com/forum/#!forum/woodstox-user) Google group 
* Check out [project Wiki](../../wiki) for javadocs

