package com.doconaut.doclet.markdown.field;

import com.doconaut.doclet.markdown.Section;
import com.doconaut.doclet.markdown.constant.ConstantField;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class FieldSection extends Section {

    private List<Field> fields;

    public FieldSection(ClassDoc c) {
        super(c, "Fields", false);

        fields = new ArrayList<Field>();

        for (FieldDoc f : c.fields()){
            if (!f.isFinal()){
                fields.add(new Field(f));
            }
        }

        Collections.sort(fields);
    }


    public String asMarkdown(){
        if (fields.isEmpty()){
            return "";
        }

        StringBuilder builder = new StringBuilder(super.asMarkdown());

        for (Field f : fields){
            builder.append(f.asMarkdown());
        }

        return builder.toString();
    }
}
