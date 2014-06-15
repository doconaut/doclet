package com.doconaut.doclet;

/**
 * By implementing this interface any class implementing this interface can be transformed to a markdown code
 * @author Hannes Dorfmann
 */
public interface Markdownable {

    /**
     * Retrieve a markdown representation of the given class
     * @return
     */
    public String asMarkdown();
}
