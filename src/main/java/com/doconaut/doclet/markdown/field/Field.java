package com.doconaut.doclet.markdown.field;

import com.doconaut.doclet.StringUtils;
import com.doconaut.doclet.markdown.MarkdownElement;
import com.sun.javadoc.FieldDoc;

/**
 * @author Hannes Dorfmann
 */
public class Field extends MarkdownElement implements Comparable<Field>{

    protected FieldDoc field;

    public Field (FieldDoc field){
        this.field = field;
    }

    @Override
    public String asMarkdown() {

        StringBuilder builder = new StringBuilder();

        builder.append("* ").append(field.modifiers()).append(" **_")
                .append(field.type().simpleTypeName()).append(" ").append(field.name()).append("_**");

        if (!StringUtils.isEmpty(field.commentText())) {
            builder.append(newLine());
            builder.append(getJavaDocResolved(field.commentText()));
        }

        return builder.toString();
    }


    @Override
    public int compareTo(Field o) {
        return this.field.name().compareTo(o.field.name());
    }
}
