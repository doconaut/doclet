package com.doconaut.doclet.markdown.method;

import com.doconaut.doclet.StringUtils;
import com.doconaut.doclet.markdown.MarkdownElement;
import com.sun.javadoc.*;

/**
 * This class represents a java markdown
 * @author Hannes Dorfmann
 */
public class Method extends MarkdownElement implements Comparable<Method> {

    private ExecutableMemberDoc method;

    public Method (ExecutableMemberDoc method){
        this.method = method;
    }


    /**
     * Get the modifiers of a markdown
     *
     * @return
     */
    private String getMethodModifiers(){

        StringBuilder modi = new StringBuilder();
        if (method.isPublic()){
            modi.append("public ");
        }

        if (method.isProtected()){
            modi.append("protected ");
        }

        if (method.isPrivate()){
            modi.append("private ");
        }

        if (method.isStatic()){
            modi.append("static ");
        }

        if (method.isFinal()){
            modi.append("final ");
        }

        if (method.isNative()){
            modi.append("native ");
        }

        if(method.isSynchronized()){
            modi.append("synchronized ");
        }

        return modi.toString().trim();

    }

    protected String getParametersForHeader(){

        StringBuilder str = new StringBuilder();

        Parameter[] par = method.parameters();
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

        return str.toString();
    }


    protected String getHeader(){
        StringBuilder builder = new StringBuilder();

        builder.append("* ");
        builder.append("**_");

        builder.append(getMethodModifiers());
        builder.append(" ");
        builder.append(method.name());
        builder.append("(");
        builder.append(getParametersForHeader());
        builder.append(")");

        builder.append("_**");

        return builder.toString();
    }

    protected String getParameterDetails(){

            ParamTag[] par = method.paramTags();

            // No parameters
            if (par.length == 0){
                return "";
            }

            StringBuilder builder = new StringBuilder();

            builder.append("    - **Parameters**");


            for (ParamTag p : par) {
                // Parameter name
                builder.append("      * *");
                builder.append(p.parameterName());
                builder.append("*");

                if (!StringUtils.isEmpty(p.parameterComment())){
                    builder.append(" - ");
                    builder.append(getJavaDocResolved(p.parameterComment()));
                }

                builder.append(newLine(false));
            }

            return builder.toString();
        }


    protected String getReturnDetails(){

        Tag[] returnTags = method.tags("return");

        if (returnTags == null || returnTags.length == 0){
            return "";
        }

        Tag ret = returnTags[0];
        StringBuilder builder = new StringBuilder();
        builder.append("    - **Returns**");
        builder.append(newLine());
        builder.append("    ");
        builder.append(ret.text());

        return builder.toString();
    }

    protected String getThrowsDetails(){

        ThrowsTag[] tags = method.throwsTags();

        if (tags == null || tags.length ==0){
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("    - **Throws**");

        for (ThrowsTag p : tags) {
            // Parameter name
            builder.append("      * *");
            builder.append(linkToClass(p.exception(), false));
            builder.append("*");

            if (!StringUtils.isEmpty(p.exceptionComment())){
                builder.append(" - ");
                builder.append(getJavaDocResolved(p.exceptionComment()));
            }

            builder.append(newLine(false));
        }

        return builder.toString();

    }


    @Override
    public String asMarkdown() {
        StringBuilder builder = new StringBuilder();

        // Write the header
        builder.append(getHeader());

        // write the description
        String description =getJavaDocResolved(method.commentText());
        if (StringUtils.isEmpty(description)) {

            builder.append(newLine());
            builder.append(description);

        }

        // Write Parameter details
        String paraDetails = getParameterDetails();
        if (!StringUtils.isEmpty(paraDetails)){

            builder.append(newLine());
            builder.append(paraDetails);

        }

        // Write Return details
        String ret = getReturnDetails();
        if (!StringUtils.isEmpty(ret)){

            builder.append(newLine());
            builder.append(ret);
        }

        // Write throws details
        String thr = getThrowsDetails();
        if (!StringUtils.isEmpty(thr)){

            builder.append(newLine());
            builder.append(thr);
        }

        return builder.toString();
    }

    @Override
    public int compareTo(Method o) {
        return this.method.name().compareTo(o.method.name());
    }
}
