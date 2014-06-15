package com.doconaut.doclet.markdown.method;

import com.doconaut.doclet.markdown.Section;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;

import java.util.Arrays;

/**
 * @author Hannes Dorfmann
 */
public class MethodSection extends Section {

    private Method[] methods;

    public MethodSection(ClassDoc c) {
        super(c, "Methods", false);

        methods = new Method[c.methods().length];
        int i = 0;
        for (MethodDoc m : c.methods()){
            methods[i++] = new Method(m);
        }


        Arrays.sort(methods);

    }


    public String asMarkdown(){
        if (methods.length == 0 ){
            return "";
        }

        StringBuilder builder = new StringBuilder(super.asMarkdown());
        for (Method m : methods) {
            builder.append(m.asMarkdown());
        }

        return builder.toString();
    }


}
