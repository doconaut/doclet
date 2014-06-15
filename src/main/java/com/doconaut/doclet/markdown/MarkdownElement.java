package com.doconaut.doclet.markdown;

import com.doconaut.doclet.Markdownable;
import com.sun.javadoc.ClassDoc;

import java.io.PrintWriter;

/**
 *
 * @author Hannes Dorfmann
 */
public abstract class MarkdownElement implements Markdownable{

    /**
     * Holds the list of prefixes that should be ignored  for generate links
     * {@link #linkToClass(com.sun.javadoc.ClassDoc, boolean)}
     */
    private static String[] NO_LINK_PREFIX = {
            "java."
    };


    /**
     * Makes a link to a class
     * @param c
     * @param qualified
     * @return
     */
    protected String linkToClass(ClassDoc c, boolean qualified) {

        // DO not link the specified objects
        for (String noLinkPref : NO_LINK_PREFIX){
            if (c.qualifiedName().startsWith(noLinkPref)) {
                return qualified ? c.qualifiedName() : c.name();
            }
        }

        return "["+ ( qualified ? c.qualifiedName() : c.name() ) +"]("+c.qualifiedName()+".html)";
    }


   /**
    * Replaces javadoc things with markdown things
    * @param textWithJavaDoc
    * @return
    */
    public String getJavaDocResolved(String textWithJavaDoc){

        if (textWithJavaDoc == null || textWithJavaDoc.length() == 0){
            return "";
        }

        String withoutJavaDoc = resolveJavaDocLink(textWithJavaDoc);

        return withoutJavaDoc;
    }

    /**
     * Resolves a javadoc link
     * @param txtWithJavaDocLink
     * @return
     */
    protected String resolveJavaDocLink(String txtWithJavaDocLink){

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

            // Replace the javadoc link with the markdown link
            txtWithJavaDocLink = txtWithJavaDocLink.replace(link, toReplace);


        }

        return txtWithJavaDocLink;
    }


    /**
     * Prints a new line with additional
     */
     protected String newLine(){
        return newLine(true);
    }

    /**
     * New line
     * @param withHtmlBr Add also the backslash n (\n) char
     */
    protected String newLine(boolean withHtmlBr){

        if (withHtmlBr){
            return " <br /> \n";
        }

        return "\n";
    }



}
