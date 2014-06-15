package com.doconaut.doclet.markdown;

import com.doconaut.doclet.StringUtils;
import com.sun.javadoc.ClassDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hannes Dorfmann
 */
public class OverviewSection extends Section {

    public OverviewSection(ClassDoc c) {
        super(c, "Overview", true);
    }


    /**
     * The list of interfaces the class implements
     * @return
     */
    protected String getInterfaces(){

        ClassDoc interfaces[] = clazz.interfaces();

        if (interfaces.length == 0){
            return "";
        }


        StringBuilder str = new StringBuilder();

        str.append(getSubSection("Implemented Interfaces"));

        str.append(newLine());

        for (int i = 0; i< interfaces.length; i++){
            ClassDoc interf = interfaces[i];
            str.append(linkToClass(interf, false));

            if (i < interfaces.length -1 ) { // If its not the last one, add a " , "
                str.append(" , ");
            }
        }

        return str.toString();
    }

    /**
     * Get the class definition like modifiers and packagename
     * @return
     */
    public String getDefinition(){

        StringBuilder builder = new StringBuilder(getSubSection("Definition"));

        builder.append(newLine());

        builder.append(clazz.modifiers());

        if (clazz.isClass()) {
            builder.append(" class");
        }

        if (clazz.isInterface()) {
            builder.append(" interface");
        }

        if (clazz.isEnum()) {
            builder.append(" enum");
        }

        builder.append(" _");
        builder.append(clazz.name());
        builder.append("_ ");

        builder.append(newLine());
        builder.append("in package _");
        builder.append(clazz.qualifiedName());
        builder.append("_");

        return builder.toString();
    }


    public String getInheritance(){

        StringBuilder str = new StringBuilder();

        str.append(getSubSection("Inheritance"));
        str.append(newLine());

        ClassDoc superClass = clazz.superclass();
        String intention = "&nbsp;&nbsp;&nbsp;";

        List<ClassDoc> superHierarchy = new ArrayList<ClassDoc>();

        // Build Hierarchy
        while (superClass != null){
            superHierarchy.add(superClass);
            superClass = superClass.superclass();
        }

        // Print hierarchy in correct order
        int intentLevel = 0;
        for (int i = superHierarchy.size()-1; i>=0; i--, intentLevel++){

            superClass = superHierarchy.get(i);

            for (int j =0; j< intentLevel; j++){
                str.append(intention);
            }

            str.append("<small>↳").append(linkToClass(superClass, true)).append("</small>");
            str.append(newLine());

        }


        // Print the class itself
        for (int j =0; j< intentLevel; j++){
            str.append(intention);
        }

        str.append("<small>↳").append(clazz.qualifiedName()).append("</small>");

        return str.toString();
    }


    @Override
    public String asMarkdown() {
        StringBuilder builder = new StringBuilder(super.asMarkdown());


        // Write the description
        builder.append(getJavaDocResolved(clazz.commentText()));
        builder.append(newLine());
        builder.append("foo");
        builder.append(newLine());


        // Definition
        builder.append(newLine());
        builder.append(getDefinition());

        // Inheritance
        builder.append(newLine());
        builder.append(getInheritance());

        // implements interfaces
        String interf = getInterfaces();
        if (!StringUtils.isEmpty(interf)) {
            builder.append(newLine());
            builder.append(interf);
        }


        // TODO generics
        // TODO Subclasses


        return builder.toString();
    }

}
