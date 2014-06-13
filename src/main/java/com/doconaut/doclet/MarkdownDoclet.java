package com.doconaut.doclet;

import com.sun.javadoc.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A doclet that creates markdown javadoc
 * @author Hannes Dorfmann
 */
public abstract class MarkdownDoclet extends Doclet{

    /**
     * Holds the list of prefixes that should be ignored  for generate links
     * {@link #linkToClass(com.sun.javadoc.ClassDoc, boolean)}
     */
    private static String[] NO_LINK_PREFIX = {
            "java."
    };


    public static boolean start(RootDoc rootDoc){

        try {
            ClassDoc[] classes = rootDoc.classes();
            for (ClassDoc c : classes) {
                writeClass(c);
            }


        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Writes the
     * @param c
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private static void writeClass(ClassDoc c) throws FileNotFoundException, UnsupportedEncodingException {

        OutputStream out = System.out;
        PrintWriter builder = new PrintWriter(out);

        // Write the headers
        writeClassHeader(c, builder);


        // Write the fields
        writeFields(c, builder);


        // constructors
        writeConstructors(c, builder);

        // methods
        writeMethods(c, builder);


        builder.flush();
        builder.close();

    }

    /**
     * Write the constructors
     * @param c
     * @param str
     */
    private static void writeConstructors(ClassDoc c, PrintWriter str) {

        writeHeadline(str, "Constructors", 2);

        ConstructorDoc[] constructors = c.constructors();

        for (ConstructorDoc con : constructors){
            writeMethod(con, str);
        }

    }

    /**
     * Write the methods
     * @param c
     * @param str
     */
    private static void writeMethods(ClassDoc c, PrintWriter str){

        if (c.methods().length == 0){
            return;
        }

        writeHeadline(str, "Methods", 2);

        MethodDoc[] methods = c.methods();

        for (MethodDoc m : methods){
            writeMethod(m, str);
        }
    }


    private static void writeMethod(ExecutableMemberDoc m, PrintWriter str){

        str.append("| ");

        // write method modifiers
        String modifiers = getMethodModifiers(m);

        if (isNotEmpty(modifiers)){
            str.append(modifiers);
            str.append(" ");
        }


        str.append("| ");

        // write name and parameters
        str.append(m.name());
        str.append("(");

        Parameter[] par = m.parameters();
        int i = 0;
        for (Parameter p : par){

           if (p.type().isPrimitive()){
               str.append(p.type().simpleTypeName());
           } else {
               str.append(linkToClass(p.type().asClassDoc(), false));
           }

            str.append(" ");
            str.append(p.name());

            if (i < par.length -1 ){
                str.append(", ");
            }

            i++;
        }

        str.append(")");

        if (isNotEmpty(m.commentText())){
            newLine(str, false);
            writeJavaDocResolved(m.commentText(), str);
        }

        str.append("\n");

    }

    /**
     * Write the headers
     *
     * @param c
     * @param str
     */
    private static void writeClassHeader(ClassDoc c, PrintWriter str){

        // modifiers
        writeModifiers(c, str);
        newLine(str);

        // Name
        str.append("# ").append(c.name());
        newLine(str);


        // inheritance
        writeInheritance(c, str);
        newLine(str);

        // interfaces
        writeInterfaces(c, str);
        newLine(str);

        // generic typ parameters
        writeTypParameters(c, str);
        newLine(str);

        // Write the comment
        writeClassOverview(c, str);
        newLine(str);

    }

    /**
     * Write the generic type parameters
     *
     * @param c
     * @param str
     */
    private static void writeTypParameters(ClassDoc c, PrintWriter str) {

        // TODO does not work

        TypeVariable [] vars = c.typeParameters();
        if (vars.length == 0){
            return;
        }

        writeHeadline(str, "Generic Type Parameters", 3);

        for (TypeVariable v : vars){

            str.append("** <").append(v.simpleTypeName()).append("> ** ");

        }

    }

    /**
     * Writes the interfacese this class is implementing
     * @param c
     * @param str
     */
    private static void writeInterfaces(ClassDoc c, PrintWriter str) {


        ClassDoc interfaces[] = c.interfaces();

        if (interfaces.length == 0){
            return;
        }


        writeHeadline(str, "Implements", 3);

        for (int i = 0; i< interfaces.length; i++){
            ClassDoc interf = interfaces[i];
            str.append(linkToClass(interf, false));

            if (i < interfaces.length -1 ) { // If its not the last one, add a " , "
                str.append(" , ");
            }
        }
    }



    /**
     * Write the fields
     * @param c
     * @param str
     */
    private static void writeFields(ClassDoc c, PrintWriter str) {

        FieldDoc [] fields = c.fields();
        if (fields.length == 0)
            return;

            ArrayList<FieldDoc> constans = new ArrayList<FieldDoc>();
            ArrayList<FieldDoc> normals = new ArrayList<FieldDoc>();

            // Distinguish between constants and normal fields
            for (FieldDoc f : fields) {

                if (f.isFinal()) {
                    constans.add(f);
                } else {
                    normals.add(f);
                }
            }

            // Print constants
            if (!constans.isEmpty()) {
                writeHeadline(str, "Constants", 2);

                for (FieldDoc f : constans) {

                    str.append(f.type().simpleTypeName()).append("|").append(f.name()).append("|");
                    writeJavaDocResolved(f.commentText(), str);
                    if (f.constantValue() != null) {

                        if (isNotEmpty(f.commentText())) {
                            newLine(str, false);
                        }

                        str.append("Constant Value: ");
                        str.append(f.constantValueExpression());
                    }
                    str.append("\n");
                }
            }


        // Print normal fields
        if (!constans.isEmpty()) {
            writeHeadline(str, "Fields", 2);

            for (FieldDoc f : normals) {

                str.append(f.type().simpleTypeName()).append("|").append(f.name()).append("|");
                writeJavaDocResolved(f.commentText(), str);
                str.append("\n");
            }
        }


    }


    private static boolean isNotEmpty(String s){
        return s != null && s.length() != 0;
    }


    /**
     * Writes the class overview
     * @param c
     * @param str
     */
    private static void writeClassOverview(ClassDoc c, PrintWriter str){
        writeHeadline(str, "Class Overview", 2);
        writeJavaDocResolved(c.commentText(), str);
    }


    /**
     * Writes the modifiers
     * @param c
     * @param str
     */
    private static void writeModifiers(ClassDoc c, PrintWriter str){

        str.append(c.modifiers());

        if (c.isClass()) {
            str.append(" class");
        }

        if (c.isInterface()) {
            str.append(" interface");
        }

        if (c.isEnum()) {
            str.append(" enum");
        }

    }


    /**
     * Get the modifiers of a method
     *
     * @param m
     * @return
     */
    private static String getMethodModifiers(ExecutableMemberDoc m){

        StringBuilder modi = new StringBuilder();

        if (m.isPublic()){
            modi.append("public ");
        }


        if (m.isProtected()){
            modi.append("protected ");
        }

        if (m.isPrivate()){
            modi.append("private ");
        }

        if (m.isStatic()){
            modi.append("static ");
        }

        if (m.isFinal()){
            modi.append("final ");
        }

        if (m.isNative()){
            modi.append("native ");
        }

        if(m.isSynchronized()){
            modi.append("synchronized ");
        }



        return modi.toString().trim();

    }

    /**
     * Prints a new line with additional \n
     * @param str
     */
    private static void newLine(PrintWriter str){
        newLine(str, true);
    }

    /**
     * New line
     * @param str
     * @param bn Add also the backslash n (\n) char
     */
    private static void newLine(PrintWriter str, boolean bn){
        str.append(" <br /> ");
        if (bn){
            str.append("\n");
        }
    }

    /**
     * Make the Inheritance tree
     * @param c
     * @param str
     */
    private static void writeInheritance(ClassDoc c, PrintWriter str){

        ClassDoc superClass = c.superclass();
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
            newLine(str);

        }


        // Print the class itself
        for (int j =0; j< intentLevel; j++){
            str.append(intention);
        }

        str.append("<small>↳").append(c.qualifiedName()).append("</small>");
        newLine(str);


    }


    /**
     * Makes a link to a class
     * @param c
     * @param qualified
     * @return
     */
    private static String linkToClass(ClassDoc c, boolean qualified) {

        // DO not link the specified objects
        for (String noLinkPref : NO_LINK_PREFIX){
            if (c.qualifiedName().startsWith(noLinkPref)) {
                return qualified ? c.qualifiedName() : c.name();
            }
        }

        return "["+ ( qualified ? c.qualifiedName() : c.name() ) +"]("+c.qualifiedName()+".html)";
    }


    /**
     * Writes a markdown headline
     * @param str
     * @param headline
     * @param level
     */
    private static void writeHeadline(PrintWriter str, String headline, int level){


        if (level<1){
            throw new IllegalArgumentException("Headline level must be 1 or greater");
        }

        for (int i = 0; i<level; i++) {
            str.append("#");
        }

        str.append(" ");
        str.append(headline);
        str.append("\n");

    }

    /**
     * Replaces javadoc things with markdown things
     * @param textWithJavaDoc
     * @return
     */
    private static PrintWriter writeJavaDocResolved(String textWithJavaDoc, PrintWriter str){

        if (textWithJavaDoc == null || textWithJavaDoc.length() == 0){
            return str;
        }

        textWithJavaDoc = resolveJavaDocLink(textWithJavaDoc);

        str.append(textWithJavaDoc);

        return str;
    }

    /**
     * Resolves a javadoc link
     * @param txtWithJavaDocLink
     * @return
     */
    private static String resolveJavaDocLink(String txtWithJavaDocLink){

        String beginStr = "{@link";
        String endStr = "}";

        boolean found = false;
        while(true){

            int index = txtWithJavaDocLink.indexOf(beginStr);
            found = index >= 0;
            if (!found){
                break;
            }

            int endLinkIndex = txtWithJavaDocLink.indexOf(endStr, index) + endStr.length();

            if (endLinkIndex < 1){
                throw new IllegalArgumentException("Missing "+endStr +" to close javadoc link in "+txtWithJavaDocLink);
            }

            String link = txtWithJavaDocLink.substring(index, endLinkIndex);

            // resolve link
            String linkUrl = link.substring(beginStr.length(), link.length()-endStr.length()).trim();

            String toReplace;
            if (linkUrl.startsWith("#")){
                // It's a link in this document
                toReplace = "["+linkUrl.substring(1)+"]("+linkUrl+")";
            } else {
                // It's a link to another document
                String urlParts [] = linkUrl.split("#");
                if (urlParts.length == 0 || urlParts.length > 1){
                    throw new IllegalArgumentException("The link contains no valid link "+ linkUrl+" in text: "+txtWithJavaDocLink);
                }

                if (urlParts.length == 1){
                    toReplace = "["+urlParts[0]+"]("+urlParts[0]+".html)";
                } else {
                    // its a link with #
                    toReplace = "["+urlParts[1]+"]("+urlParts[0]+".html#"+urlParts[1]+")";
                }
            }

            System.out.println("ToReplace "+toReplace+ " "+link);

            // Replace the javadoc link with the markdown link
            txtWithJavaDocLink = txtWithJavaDocLink.replace(link, toReplace);


        }

        return txtWithJavaDocLink;
    }


}
