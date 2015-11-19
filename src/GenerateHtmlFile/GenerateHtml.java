package GenerateHtmlFile; /**
 * Created by user on 10.10.15.
 */

import java.io.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import org.apache.commons.io.FileUtils;

public class GenerateHtml {

    public GenerateHtml()throws IOException {

        File htmlTemplateFile = new File("html/index.html");
        htmlTemplateString = FileUtils.readFileToString(htmlTemplateFile);
       /* String title = "New Page";
        String body = "This is Body";
        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$body", body);
        File newHtmlFile = new File("path/new.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);
        */
    }

    public String GetIndex(String name)throws IOException {

        File htmlTemplateContentFile = new File("html/content.html");
        String htmlDynamicContent = FileUtils.readFileToString(htmlTemplateContentFile);

        htmlDynamicContent = htmlDynamicContent.replace("$name", name);

        String htmlString = htmlTemplateString;
        htmlString = htmlString.replace("$title", "Главная страница");
        htmlString = htmlString.replace("$body", htmlDynamicContent);

        return htmlString;
    }



    public String GetCurrentUser(String name)throws IOException {
        File htmlTemplateContentFile = new File("html/content.html");
        String htmlDynamicContent = FileUtils.readFileToString(htmlTemplateContentFile);

        htmlDynamicContent = htmlDynamicContent.replace("$name", name);

        String htmlString = htmlTemplateString;
       // htmlString = htmlString.replace("$title", "Users");
        htmlString = htmlString.replace("$body", htmlDynamicContent);

        return htmlString;
    }

    public String GetRegis() throws IOException {
        File htmlTemplateContentFile = new File("html/regis.html");
        String htmlDynamicContent = FileUtils.readFileToString(htmlTemplateContentFile);

        String htmlString = htmlTemplateString;
        htmlString = htmlString.replace("$body", htmlDynamicContent);

        return htmlString;
    }

    public String GetAutho() throws IOException {
        File htmlTemplateContentFile = new File("html/autho.html");
        String htmlDynamicContent = FileUtils.readFileToString(htmlTemplateContentFile);

        String htmlString = htmlTemplateString;
        htmlString = htmlString.replace("$body", htmlDynamicContent);

        return htmlString;
    }

    public String getRegisForm()throws IOException {
        File htmlTemplateContentFile = new File("html/regis.html");
        String htmlDynamicContent = FileUtils.readFileToString(htmlTemplateContentFile);

      //  String htmlString = htmlTemplateString;
      //  htmlString = htmlString.replace("$body", htmlDynamicContent);

        return htmlDynamicContent;
    }




    public String GetContent(String content)
    {
        String htmlString = htmlTemplateString;
        // htmlString = htmlString.replace("$title", "Users");
        htmlString = htmlString.replace("$body", content);
        return htmlString;
    }


    private String htmlTemplateString;

}
