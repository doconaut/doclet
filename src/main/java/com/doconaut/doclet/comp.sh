javac MarkdownDoclet.java
mv MarkdownDoclet.class com/doconaut/doclet/MarkdownDoclet.class 
javadoc -doclet com.doconaut.doclet.MarkdownDoclet MarkdownDoclet.java -private
