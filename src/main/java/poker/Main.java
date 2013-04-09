package poker;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.*;
import spark.utils.IOUtils;

public class Main {

	public static void main(String[] args) {

		staticFileRoute("/content");
		
		get(new Route("/hello") {
			@Override
			public Object handle(Request request, Response response) {

				//Thread.currentThread().getContextClassLoader().getResourceAsStream("content/templates/test.ftl")
				
				Configuration cfg = new Configuration();
				try {
					File file = new File(Thread.currentThread().getContextClassLoader().getResource("content/templates/").toURI());
					cfg.setDirectoryForTemplateLoading(file);
					cfg.setObjectWrapper(new DefaultObjectWrapper());

					/* Get or create a template */
					Template temp = cfg.getTemplate("test.ftl");

					/* Create a data-model */
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("user", "Big Joe");
					Map<String, Object> latest = new HashMap<String, Object>();
					root.put("latestProduct", latest);
					latest.put("url", "products/greenmouse.html");
					latest.put("name", "green mouse");

					/* Merge data-model with template */
					
					Writer out = new StringWriter();
					temp.process(root, out);
					return out.toString();

				} catch (IOException | TemplateException | URISyntaxException e) {
					e.printStackTrace();
				}
				
				return "Hello World...";
			}
		});

	}

}