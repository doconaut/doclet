package com.doconaut.doclet;

import com.doconaut.doclet.markdown.OverviewSection;
import com.doconaut.doclet.markdown.constant.ConstantsSection;
import com.doconaut.doclet.markdown.constructor.ConstructorSection;
import com.doconaut.doclet.markdown.field.FieldSection;
import com.doconaut.doclet.markdown.method.MethodSection;
import com.sun.javadoc.*;

import java.io.*;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * A doclet that creates markdown javadoc
 * @author Hannes Dorfmann
 */
public abstract class MarkdownDoclet extends Doclet{


    private static final String JAVADOC_TEMPLATE_CONTENT_PLACEHOLDER = "{{ doconaut.container }}";

    private static final String JAVADOC_TEMPLATE_NAME = "{{ doconaut.name }}";

    private static final String JAVADOC_TEMPLATE_PREMALINKT = "{{ doconaut.permalink }}";


    /**
     * Where to store the generated files
     */
    private static final String TARGET_DIR = "docs";

    /**
     * Specify the path to the template file that should be loaded
     */
    private static final String PARAM_TEMPLATE = "-template";


    // TODO find a solution for that
    /**
     * Specify the path where the generated javadoc should be stored
     */
    private static final String PARAM_TARGET = "-target";


    private static File templatePage;

    public static boolean start(RootDoc rootDoc){

        String targetPath = "/Users/hannes/jekyll/hmfaysal-omega-theme/docs/";
        File targetFolder = new File(targetPath);
        deleteFolder(targetFolder);
        targetFolder.mkdirs();

        try {
            ClassDoc[] classes = rootDoc.classes();
            for (ClassDoc c : classes) {
                writeClass(c, targetPath);
            }


        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Validate and read the
     * @param options
     * @param reporter
     * @return
     */
    public static boolean validOptions(String options[][],
                                       DocErrorReporter reporter) {
        boolean foundTagOption = false;
        for (int i = 0; i < options.length; i++) {
            String[] opt = options[i];
            if (opt[0].equals(PARAM_TEMPLATE)) {
                if (foundTagOption) {
                    reporter.printError("Only one "+ PARAM_TEMPLATE +" option allowed.");
                    return false;
                } else {

                    templatePage = new File(opt[1]);
                    if (!templatePage.exists()){
                        reporter.printError("The specified page templatePage does not exists, maybe the path to this templatePage is wrong");
                        return false;
                    }
                    foundTagOption = true;
                }
            }
        }


       return true;
    }

    public static int optionLength(String option) {

        if(option.equals(PARAM_TEMPLATE)) {
            return 2;
        }

        return 0;
    }

    private static String readTemplateFile() throws IOException{
        BufferedReader br = null;

        StringBuilder builder = new StringBuilder();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(templatePage));

            while ((sCurrentLine = br.readLine()) != null) {
                builder.append(sCurrentLine);
                builder.append("\n");
            }

            return builder.toString();


        } finally {

            if (br != null)
                br.close();

        }
    }


    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    /**
     * Generates and writes
     * @param c
     * @param targetDir The target directory where to write
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    private static void writeClass(ClassDoc c, String targetDir) throws IOException {

        String name = c.name();
        String permalink = c.qualifiedName();




        OutputStream out = System.out;
        PrintWriter builder = new PrintWriter(targetDir+permalink+".md");

        // Template things, if there is anything
        String template = null;
        String remainingTemplate = null;
        if (templatePage!=null){
            template = readTemplateFile();

            // Set title
            template = template.replace(JAVADOC_TEMPLATE_NAME, name);

            // Set permalink
            template = template.replace(JAVADOC_TEMPLATE_PREMALINKT, TARGET_DIR+(TARGET_DIR.endsWith("/")? "" : "/") + permalink+".html");

            // Set content
            int startIndex = template.indexOf(JAVADOC_TEMPLATE_CONTENT_PLACEHOLDER);
            if (startIndex == -1){
                throw new IOException("The template file does not contain the required placeholder with "+ JAVADOC_TEMPLATE_CONTENT_PLACEHOLDER);
            }

            builder.append(template.substring(0, startIndex));

            remainingTemplate = template.substring(startIndex + JAVADOC_TEMPLATE_CONTENT_PLACEHOLDER.length());

        }


        // Write sections

        builder.append(new OverviewSection(c).asMarkdown());
        builder.append(new ConstructorSection(c).asMarkdown());
        builder.append(new ConstantsSection(c).asMarkdown());
        builder.append(new FieldSection(c).asMarkdown());
        builder.append(new MethodSection(c).asMarkdown());


        if (remainingTemplate != null) {
            builder.append(remainingTemplate);
        }

        builder.flush();
        builder.close();


    }

}
