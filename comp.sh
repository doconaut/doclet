javac src/main/java/com/doconaut/doclet/MarkdownDoclet.java
mv src/main/java/com/doconaut/doclet/MarkdownDoclet.class build/com/doconaut/doclet/MarkdownDoclet.class
javadoc -doclet com.doconaut.doclet.MarkdownDoclet -sourcepath src/main/java/ -docletpath build/ -private -subpackages com -template /Users/hannes/jekyll/hmfaysal-omega-theme/_doconaut/javadoc.md
