#!/bin/sh
ant jar
javadoc -doclet com.doconaut.doclet.MarkdownDoclet -docletpath build/doconaut.jar -sourcepath src/main/java/ -docletpath build/ -private -subpackages com -template /Users/hannes/jekyll/hmfaysal-omega-theme/_doconaut/javadoc.md
