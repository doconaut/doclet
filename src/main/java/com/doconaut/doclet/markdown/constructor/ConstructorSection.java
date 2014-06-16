package com.doconaut.doclet.markdown.constructor;

import com.doconaut.doclet.markdown.Section;
import com.doconaut.doclet.markdown.method.MethodsComparator;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;

import java.util.Arrays;

/**
 * @author Hannes Dorfmann
 */
public class ConstructorSection extends Section {

    private Constructor[] constructors;

    public ConstructorSection(ClassDoc c) {
        super(c, "Constructors", false);

        ConstructorDoc [] cons = c.constructors();

        constructors = new Constructor[cons.length];

        int i = 0;
        for (ConstructorDoc co : cons){
            constructors[i++] = new Constructor(co);
        }

        Arrays.sort(constructors, new MethodsComparator());

    }


    public String asMarkdown(){


        if (constructors.length == 0){
            return "";
        }

        StringBuilder builder = new StringBuilder(super.asMarkdown());

        for (Constructor c : constructors){
            builder.append(c.asMarkdown());
        }

        return builder.toString();
    }


}
