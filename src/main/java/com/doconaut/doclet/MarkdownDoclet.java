package com.doconaut.doclet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

import java.io.*;

/**
 * Created by hannes on 12.06.14.
 * @author Hannes Dorfmann
 */
public abstract class MarkdownDoclet extends Doclet{

    /**
     * Holds the list of prefixes that should be ignored  for generate links
     * {@link #linkToClass(com.sun.javadoc.ClassDoc, boolean)}
     */
    private static String[] NO_LINK_PREFIX = {
            "java.lang"
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



    private static void writeClass(ClassDoc c) throws FileNotFoundException, UnsupportedEncodingException {

        OutputStream out = System.out;
        PrintWriter builder = new PrintWriter(out);

        writeClassHeader(c, builder);
        builder.flush();
        builder.close();

    }

    /**
     * Write the headers
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

        // Write the comment
        writeClassOverview(c, str);
        newLine(str);


        writeFields(c, str);
    }


    /**
     * Writes the javadoc comments with already resolving the javadoc links
     * @param comment
     * @param str
     */
    private static PrintWriter writeComments(String comment, PrintWriter str){

        if (comment == null || comment.length() == 0){
            return str;
        }

        comment = resolveJavaDocLink(comment);

        str.append(comment);

        return str;
    }
    /**
     * Write the fields
     * @param c
     * @param str
     */
    private static void writeFields(ClassDoc c, PrintWriter str) {

        FieldDoc [] fields = c.fields();
        if (fields.length > 0) {
            writeHeadline(str, "Constants", 2);

            for (FieldDoc f : fields) {
                str.append(f.type().simpleTypeName()).append("|").append(f.name()).append("|");
                writeComments(f.commentText(), str).append("\n");
            }
        }

    }


    /**
     * Writes the class overview
     * @param c
     * @param str
     */
    private static void writeClassOverview(ClassDoc c, PrintWriter str){
        writeHeadline(str, "Class Overview", 2);
        writeComments(c.commentText(), str);
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
     * New line
     * @param str
     */
    private static void newLine(PrintWriter str){
        str.append("<br />\n");
    }

    /**
     * Make the Inheritance tree
     * @param c
     * @param str
     */
    private static void writeInheritance(ClassDoc c, PrintWriter str){

        ClassDoc superClass = c.superclass();
        String intention = "&nbsp;&nbsp;&nbsp;";

        int intentLevel = 0;
        while (superClass != null){

            for (int i =0; i< intentLevel; i++){
                str.append(intention);
            }

            str.append("<small>↳").append(linkToClass(superClass, true)).append("</small>");
            newLine(str);

            superClass = superClass.superclass();
            intentLevel++;
        }

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
