# orchestra2md

The orchestra2md utilty documents an Orchestra repository as a markdown document.

## Running md2orchestra

### Command line arguments

```
usage: Md2Orchestra
 -?,--help              display usage
 -e,--eventlog <arg>    path of log file
 -i,--input <arg>       path of markdown input file
 -o,--output <arg>      path of output Orchestra file
 -r,--reference <arg>   path of reference Orchestra file
 -v,--verbose           verbose event log
 ```

### Invoked from an application

The utility may be invoked from Java code as a library. It is constructed and configured by its `Builder` class.

Example

```java
Orchestra2md orchestra2md = Orchestra2md.builder()
    .inputFile("myorchestra.xml")
    .outputFile("mymarkdown.md").build();
orchestra2md.generate();
```