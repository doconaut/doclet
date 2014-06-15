package com.doconaut.doclet.markdown;

import com.sun.javadoc.ClassDoc;

/**
 * @author Hannes Dorfmann
 */
public class Section extends MarkdownElement {

    protected ClassDoc clazz;
    protected boolean isFirstSection;
    protected String title;

    public Section(ClassDoc c, String title, boolean isFirst){
        this.clazz = c;
        this.isFirstSection = isFirst;
        this.title = title;
    }

    @Override
    public String asMarkdown() {
        if (isFirstSection) {
            return "";
        }
        else
        {
            StringBuilder builder = new StringBuilder();
            builder.append("* * *");
            builder.append(newLine(false));
            builder.append(newLine(false));
            builder.append("# ");
            builder.append(title);
            builder.append(newLine(false));
            builder.append(newLine(false));
            return builder.toString();
        }
    }

    protected String getSubSection(String title){
        return "**"+title+"**" + newLine(false);

    }
}
