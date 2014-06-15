package com.doconaut.doclet.markdown.constant;

import com.doconaut.doclet.markdown.field.Field;
import com.sun.javadoc.FieldDoc;

/**
 * @author Hannes Dorfmann
 */
public class ConstantField extends Field {

    public ConstantField(FieldDoc field) {
        super(field);
    }

    @Override
    public String asMarkdown() {
        StringBuilder builder = new StringBuilder(super.asMarkdown());

        builder.append("    - _Constant Value_");
        builder.append(newLine());
        builder.append(field.constantValueExpression());

        return builder.toString();
    }
}
