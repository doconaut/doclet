#!/bin/sh
mvn install
javadoc -doclet com.doconaut.doclet.MarkdownDoclet -docletpath target/doclet-1.0-SNAPSHOT.jar -sourcepath src/main/java/ -docletpath build/ -private -subpackages com -template /Users/hannes/jekyll/hmfaysal-omega-theme/_doconaut/javadoc.md
