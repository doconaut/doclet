package com.doconaut.doclet.markdown.method;

import java.util.Comparator;

/**
 * @author Hannes Dorfmann
 */
public class MethodsComparator implements Comparator<Method> {

    @Override
    public int compare(Method o1, Method o2) {
        return o1.compareTo(o2);
    }
}
