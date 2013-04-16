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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poker.entities.Estimate;
import poker.entities.Story;
import poker.entities.Task;
import poker.entities.UnitType;
import poker.entities.User;
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
				root.put("task", new Task("",""));
				root.put("edit", false);
				return render("task_info.ftl", cfg, root);
			}

		});

		post(new Route("/task/new") {

			@Override
			public Object handle(Request request, Response response) {

				int id = dm.insertTask(new Task(request.queryParams("task_name"), request
						.queryParams("task_description")));
				dm.createFibonacciEstimations(id);

				response.redirect("/task/" + id + "/edit/info");
				return null;
			}
		});

		get(new Route("/task/:id/edit/info") {

			@Override
			public Object handle(Request request, Response response) {
				int task_id = Integer.parseInt(request.params(":id"));
				Map<String, Object> root = new HashMap<String, Object>();
				root.put("task", dm.getTask(task_id));
				root.put("users", dm.getUsers());
				root.put("task_users", dm.getUsersFromTask(task_id));
				root.put("edit", true);
				
				return render("task_info.ftl", cfg, root);
			}

		});
		
		post(new Route("/task/:id/edit/info") {

			@Override
			public Object handle(Request request, Response response) {
				int task_id = Integer.parseInt(request.params(":id"));
				Task t = dm.getTask(task_id);
				String name = request.queryParams("task_name");
				String desc = request.queryParams("task_description");
				
				t.setName(name);
				t.setDescription(desc);
				
				dm.setTask(t);
				
				response.redirect(String.format("/task/%d/edit/info", task_id));
				return null;
			}

		});
		
		post(new Route("/task/:id/edit/user/create") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));
				
				String name = request.queryParams("user_name");
				User user = new User(name);
				dm.insertUser(user);				
				
				response.redirect(String.format("/task/%d/edit/info", task_id));
				return null;
			}
		});
		
		post(new Route("/task/:id/edit/user/add") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));
				
				int user_id = Integer.parseInt(request.queryParams("user"));
				dm.addUserToTask(task_id, user_id);
				
				response.redirect(String.format("/task/%d/edit/info", task_id));
				return null;
			}
		});
		
		get(new Route("/task/:id/edit/user/:user_id/remove") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));
				
				int user_id = Integer.parseInt(request.params(":user_id"));
				dm.deleteUserFromTask(task_id, user_id);
				
				response.redirect(String.format("/task/%d/edit/info", task_id));
				return null;
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

					// we still need to set the new unit type on all estimations!
					estimate.setUnit(UnitType.values()[unit-1]);
					dm.setEstimate(estimate);
				}
				
				response.redirect("/task/" + task_id + "/edit/estimations");
				return null;
			}

		});

		get(new Route("/task/:id/edit/stories") {
			@Override
			public Object handle(Request request, Response response) {

				/* Create a data-model */
				Map<String, Object> root = new HashMap<String, Object>();
				int task_id = Integer.parseInt(request.params("id"));
				Task t = dm.getTask(task_id);
				root.put("task", t);
				root.put("stories", dm.getStoriesFromTask(task_id));
				root.put("published", t.getPublishedAt() != null);
				
				return render("task_stories.ftl", cfg, root);
			}

		});

		post(new Route("/task/:id/edit/stories") {

			@Override
			public Object handle(Request request, Response response) {

				response.redirect("/index");
				return null;
			}

		});
		
		get(new Route("/task/:task_id/story/:story_id/delete") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":task_id"));
				int story_id = Integer.parseInt(request.params(":story_id"));
				
				dm.deleteStory(story_id);
				
				response.redirect(String.format("/task/%d/edit/stories", task_id));
				return null;
			}
		});
		
		post(new Route("/task/:id/story/add") {
			@Override
			public Object handle(Request request, Response response) {
				System.out.println("Adding story");
				int task_id = Integer.parseInt(request.params(":id"));
				String story_name = request.queryParams("story_name");
				String story_desc = request.queryParams("story_description");
				
				dm.insertStory(new Story(task_id,story_name,story_desc));
				
				response.redirect(String.format("/task/%d/edit/stories", task_id));
				return null;
			}
		});
		
		post(new Route("/task/:id/publish") {
			@Override
			public Object handle(Request request, Response response) {
				int task_id = Integer.parseInt(request.params(":id"));
				Task t = dm.getTask(task_id);
				t.setPublishedAt(new java.sql.Date(new java.util.Date().getTime()));
				dm.setTask(t);
				
				response.redirect("/");
				return null;
			}
		});
		
		post(new Route("/task/:id/unpublish") {
			@Override
			public Object handle(Request request, Response response) {
				int task_id = Integer.parseInt(request.params(":id"));
				Task t = dm.getTask(task_id);
				t.setPublishedAt(null);
				dm.setTask(t);
				
				response.redirect(String.format("/task/%d/edit/stories", task_id));
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
				List<HashMap<User, List<Estimate>>> story_estimations = new ArrayList<HashMap<User, List<Estimate>>>();
				for (Story s : storiesFromTask) {
					s.setEstimations(dm.getEstimatesFromStory(s.getId()));
					story_estimations.add(dm.getEstimatesFromStory(s.getId()));
				}
				root.put("stories", storiesFromTask);
				root.put("users", dm.getUsersFromTask(task_id));
				root.put("estimations", dm.getEstimationsForTask(task_id));
				root.put("story_estimations", story_estimations);

				return render("poker.ftl", cfg, root);
			}
		});

		get(new Route("/ajax") {
			@Override
			public Object handle(Request request, Response response) {
				
				Map<String, Object> root = new HashMap<String, Object>();
				return render("ajax.ftl", cfg, root);
			}
		});
		
		post(new Route("/ajax") {
			@Override
			public Object handle(Request request, Response response) {
			
				return "Hello World!";
			}
		});
		
	}
}
