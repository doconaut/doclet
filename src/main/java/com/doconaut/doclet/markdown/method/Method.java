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

    protected String getParametersForHeader(){

        String signature = method.signature();

        Parameter[] par = method.parameters();
        for (Parameter p : par){

            if (!p.type().isPrimitive()){

                signature = signature.replace(p.name(), linkToClass(p.type().asClassDoc(), false));
            }

        }

        return signature;
    }

    protected Type getReturnType(){
        if (method instanceof MethodDoc){
            return ((MethodDoc) method).returnType();
        }

        return null;
    }


    protected String getHeader(){
        StringBuilder builder = new StringBuilder();

        builder.append("* ");

        builder.append(method.modifiers()).append(" **_");


        // The return type
        Type returnType = getReturnType();
        if (returnType != null){
            if(returnType.isPrimitive()){
                builder.append(returnType.simpleTypeName()).append(" ");
            } else {
                builder.append(linkToClass(returnType.asClassDoc(), false)).append(" ");
            }
        }

        builder.append(method.name());
        builder.append("_** ");
        builder.append(getParametersForHeader());


        return builder.toString();
    }

    protected String getParameterDetails(){

            ParamTag[] par = method.paramTags();

            // No parameters
            if (par.length == 0){
                return "";
            }

            StringBuilder builder = new StringBuilder();

            builder.append("    - _Parameters_");
            builder.append(newLine(false));


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
        builder.append("    - _Returns_");
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
        builder.append("    - _Throws_");
        builder.append(newLine(false));

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
        if (!StringUtils.isEmpty(description)) {

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

        builder.append(newLine());

        return builder.toString();
    }

    @Override
    public int compareTo(Method o) {

        int c = this.method.name().compareTo(o.method.name());

        if (c != 0){
            return  c;
        }

        return this.method.parameters().length - o.method.parameters().length;
    }
}
