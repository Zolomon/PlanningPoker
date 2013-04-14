package poker;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poker.entities.Estimate;
import poker.entities.Story;
import poker.entities.Task;
import spark.Request;
import spark.Response;
import spark.Route;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Main {
	private static DatabaseManager	dm;

	private void debug(String msg) {
		System.out.println(msg);
	}

	private static String render(String filename, Configuration cfg, Map<String, Object> root) {
		try {

			/* Get or create a template */
			Template temp = cfg.getTemplate(filename);

			/* Merge data-model with template */

			Writer out = new StringWriter();
			temp.process(root, out);
			return out.toString();

		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}

		return "Hello World...";
	}

	public static void main(String[] args) throws ClassNotFoundException {

		// SQLite setup section
		Class.forName("org.sqlite.JDBC");

		dm = new DatabaseManager(System.out);

		final Configuration cfg = new Configuration();
		try {

			File file = new File(Thread.currentThread().getContextClassLoader().getResource("content/templates/")
					.toURI());

			cfg.setDirectoryForTemplateLoading(file);
			cfg.setObjectWrapper(new DefaultObjectWrapper());

		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}

		staticFileLocation("/content");

		get(new Route("/index") {
			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("tasks", dm.getTasks());
				
				return render("index.ftl", cfg ,root);
			}
		});
		
		get(new Route("/tasks/edit/info/:id") {

			@Override
			public Object handle(Request request, Response response) {
				  return "Hello: " + request.params(":id");
			}
			
		});
		
		get(new Route("/tasks/new") {

			@Override
			public Object handle(Request request, Response response) {
				
				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				return  render("newtask.ftl", cfg, root);
			}
			
		});
		
		post(new Route("/tasks/new/process"){

			@Override
			public Object handle(Request request, Response response) {
				
			int id = dm.insertTask(new Task(request.queryParams("taskname"), request.queryParams("taskdescription")));
			response.redirect("/tasks/edit/estimationsettings/" + id);
			return null;
			}
		});
		
		get(new Route("/tasks/edit/estimationsettings/:id") {

			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("task", dm.getTask(Integer.parseInt(request.params("id"))));
				return render("taskestimations.ftl", cfg, root);
			}
			
		});
		
		post(new Route("/tasks/edit/estimationsettings/process") {

			@Override
			public Object handle(Request request, Response response) {

				//insert operation krävs!!!!
				response.redirect("/tasks/edit/stories/" + request.queryParams("taskid"));
				return null;
			}
			
		});
		
		get(new Route("/tasks/edit/stories/:id") {

			@Override
			public Object handle(Request request, Response response) {
				

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("task", dm.getTask(Integer.parseInt(request.params("id"))));
				return render("taskstories.ftl", cfg, root);
			}
			
		});
		
		post(new Route("/tasks/edit/stories/process") {

			@Override
			public Object handle(Request request, Response response) {

				//insert operation krävs!!!!
				response.redirect("/index");
				return null;
			}
			
		});

		post(new Route("/task") {
			@Override
			public Object handle(Request request, Response response) {

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("taskname", request.queryParams("taskname"));

				return render("task.ftl", cfg, root);
			}
		});

		get(new Route("/summary/:id") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));
				
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("id", task_id);
				
				List<Story> stories = dm.getStoriesFromTask(task_id);
				root.put("stories", stories);
				
				return render("summary.ftl", cfg, root);
			}
		});

	}

}
