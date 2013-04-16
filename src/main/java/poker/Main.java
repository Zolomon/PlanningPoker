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
import poker.entities.UnitType;
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

		get(new Route("/") {
			@Override
			public Object handle(Request request, Response response) {

				System.out.println("Index page reached!");
				response.redirect("/tasks", 302);
				return null;
			}
		});

		get(new Route("/tasks") {
			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				List<Task> tasks = dm.getTasks();
				for (Task t : tasks) {
					t.setUsers(dm.getUsersFromTask(t.getId()));
				}
				root.put("tasks", tasks);

				return render("tasks.ftl", cfg, root);
			}
		});

		get(new Route("/task/new") {

			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				return render("task_new.ftl", cfg, root);
			}

		});

		post(new Route("/task/new") {

			@Override
			public Object handle(Request request, Response response) {

				int id = dm.insertTask(new Task(request.queryParams("task_name"), request
						.queryParams("taskd_escription")));
				dm.createFibonacciEstimations(id);

				response.redirect("/task/" + id + "/edit/estimations");
				return null;
			}
		});

		get(new Route("/task/:id/edit/info") {

			@Override
			public Object handle(Request request, Response response) {
				return "Here we should actually render the same as /tasks/new and allow edit of task with id "
						+ request.params(":id");
			}

		});

		get(new Route("/task/:id/edit/estimations") {

			@Override
			public Object handle(Request request, Response response) {
				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				int task_id = Integer.parseInt(request.params(":id"));
				root.put("task", dm.getTask(task_id));
				root.put("complexities", dm.getEstimationsForTask(task_id));

				return render("task_estimations.ftl", cfg, root);
			}

		});

		post(new Route("/task/:id/edit/estimations") {

			@Override
			public Object handle(Request request, Response response) {

				// insert operation krävs!!!!
				// dm.insertEstimate(new
				// Estimate(Integer.parse(request.queryParams("taskid")),
				// complexity_symbol, unit, unit_value))
				int task_id = Integer.parseInt(request.params(":id"));
				
				int unit = 1;
				if (request.queryParams("estimation_unit").toLowerCase().equals("person days")) unit = 2;
				if (request.queryParams("estimation_unit").toLowerCase().equals("person months")) unit = 3;
				if (request.queryParams("estimation_unit").toLowerCase().equals("person years")) unit = 4;
								
				List<Estimate> task_estimations = dm.getEstimationsForTask(task_id);
				
				for (Estimate estimate : task_estimations) {
					try {
						float parsedValue = Float.parseFloat(request.queryParams(String.format("complexity-%d", estimate.getId())));
						estimate.setUnitValue(parsedValue);
					} catch (NullPointerException | NumberFormatException e) {
						// no new (or strange) value, let's happily skip it!
					}
					
					estimate.setUnit(UnitType.values()[unit-1]);
					dm.setEstimate(estimate);
				}
				
				response.redirect("/task/" + task_id + "/edit/stories");
				return null;
			}

		});

		get(new Route("/task/:id/edit/stories") {
			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("task", dm.getTask(Integer.parseInt(request.params("id"))));
				return render("task_stories.ftl", cfg, root);
			}

		});

		post(new Route("/task/:id/edit/stories") {

			@Override
			public Object handle(Request request, Response response) {

				// insert operation krävs!!!!
				response.redirect("/index");
				return null;
			}

		});

		get(new Route("/task/:id/summary") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("id", task_id);

				List<Story> stories = dm.getStoriesFromTask(task_id);
				root.put("stories", stories);

				return render("task_summary.ftl", cfg, root);
			}
		});

		get(new Route("/poker/:task_id/:user_id") {
			@Override
			public Object handle(Request request, Response response) {

				Map<String, Object> root = new HashMap<String, Object>();
				int task_id = Integer.parseInt(request.params(":task_id"));
				List<Story> storiesFromTask = dm.getStoriesFromTask(task_id);
				for (Story s : storiesFromTask) {
					s.setEstimations(dm.getEstimatesFromStory(s.getId()));
				}
				root.put("stories", storiesFromTask);
				root.put("users", dm.getUsersFromTask(task_id));

				return render("poker.ftl", cfg, root);
			}
		});

	}

}
