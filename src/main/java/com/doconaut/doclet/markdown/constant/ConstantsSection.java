package com.doconaut.doclet.markdown.constant;

import com.doconaut.doclet.markdown.Section;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Hannes Dorfmann
 */
public class ConstantsSection extends Section {

    private ArrayList<ConstantField> constants;

    public ConstantsSection(ClassDoc c) {
        super(c, "Constants", false);

        constants = new ArrayList<ConstantField>();
        for (FieldDoc f : c.fields()){
            if (f.isFinal()){
                constants.add(new ConstantField(f));
            }
        }

        Collections.sort(constants);

    }

    public String asMarkdown(){

        if (constants.size() == 0){
            return "";
        }

        StringBuilder builder = new StringBuilder(super.asMarkdown());

        for (ConstantField f : constants){
            builder.append(f.asMarkdown());
        }

        return builder.toString();
    }
}
