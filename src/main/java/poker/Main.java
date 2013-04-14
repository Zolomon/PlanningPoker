package poker;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import poker.entities.Task;
import spark.Request;
import spark.Response;
import spark.Route;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Main {
	private static DatabaseManager dm;

	public static void main(String[] args) throws ClassNotFoundException {

		// SQLite setup section
		Class.forName("org.sqlite.JDBC");

		dm = new DatabaseManager(System.out);


		final Configuration cfg = new Configuration();
		try {

			File file = new File(Thread.currentThread().getContextClassLoader()
					.getResource("content/templates/").toURI());

			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}

		staticFileLocation("/content");
		
		get(new Route("/index") {
			@Override
			public Object handle(Request request, Response response) {

				try {

					/* Get or create a template */
					Template temp = cfg.getTemplate("index.ftl");

					/* Create a data-model */
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("tasks", dm.getTasks());

					/* Merge data-model with template */

					Writer out = new StringWriter();
					temp.process(root, out);
					return out.toString();

				} catch (IOException | TemplateException | SQLException e) {
					e.printStackTrace();
				}

				return "Hello World...";
			}
		});
		
		get(new Route("/tasks/edit/info/:id") {

			@Override
			public Object handle(Request request, Response response) {
				  return "Hello: " + request.params(":id");
			}
			
		});
		
		get(new Route("/new") {

			@Override
			public Object handle(Request request, Response response) {
				try {

					/* Get or create a template */
					Template temp = cfg.getTemplate("newtask.ftl");

					/* Create a data-model */
					Map<String, Object> root = new HashMap<String, Object>();

					/* Merge data-model with template */

					Writer out = new StringWriter();
					temp.process(root, out);
					return out.toString();

				} catch (IOException | TemplateException e) {
					e.printStackTrace();
				}
				return "new task";
			}
			
		});

		get(new Route("/hello") {
			@Override
			public Object handle(Request request, Response response) {

				try {

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

				} catch (IOException | TemplateException e) {
					e.printStackTrace();
				}

				return "Hello World...";
			}
		});

		post(new Route("/task") {
			@Override
			public Object handle(Request request, Response response) {

				try {
					/* Get or create a template */
					Template temp = cfg.getTemplate("task.ftl");

					/* Create a data-model */
					Map<String, Object> root = new HashMap<String, Object>();
					root.put("taskname", request.queryParams("taskname"));

					System.out.println(request.queryParams("taskname"));

					Writer out = new StringWriter();
					temp.process(root, out);
					return out.toString();

				} catch (IOException | TemplateException e) {
					e.printStackTrace();
				}

				return "Hello World...";
			}
		});

	}

}