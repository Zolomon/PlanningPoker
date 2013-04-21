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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import poker.entities.Estimate;
import poker.entities.Story;
import poker.entities.StoryEstimate;
import poker.entities.Task;
import poker.entities.UnitType;
import poker.entities.User;
import poker.entities.UserEstimate;
import spark.JettyLogger;
import spark.Request;
import spark.Response;
import spark.Route;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Main {
	private static final int		NO_CONSENSUS	= -1;
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

		JettyLogger jl = new JettyLogger();

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
				root.put("task", new Task("", ""));
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
				if (request.queryParams("estimation_unit").toLowerCase().equals("person days"))
					unit = 2;
				if (request.queryParams("estimation_unit").toLowerCase().equals("person months"))
					unit = 3;
				if (request.queryParams("estimation_unit").toLowerCase().equals("person years"))
					unit = 4;

				List<Estimate> task_estimations = dm.getEstimationsForTask(task_id);

				for (Estimate estimate : task_estimations) {
					try {
						float parsedValue = Float.parseFloat(request.queryParams(String.format("complexity-%d",
								estimate.getId())));
						estimate.setUnitValue(parsedValue);
					} catch (NullPointerException | NumberFormatException e) {
						// no new (or strange) value, let's happily skip it!
					}

					// we still need to set the new unit type on all
					// estimations!
					estimate.setUnit(UnitType.values()[unit - 1]);
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

				dm.insertStory(new Story(task_id, story_name, story_desc));

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

				List<StoryEstimate> stories = new ArrayList<StoryEstimate>();
				for (Story s : dm.getStoriesFromTask(task_id)) {
					stories.add(new StoryEstimate(s, (s.getConsensus() == -1 ? 0 : dm.getEstimate(s.getConsensus())
							.getUnitValue())));
				}
				root.put("stories", stories);

				return render("task_summary.ftl", cfg, root);
			}
		});

		get(new Route("/task/:id/summary/unit/:unit_id") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":id"));
				int unit_id = Integer.parseInt(request.params(":unit_id"));

				Map<String, Object> root = new HashMap<String, Object>();
				root.put("id", task_id);

				List<StoryEstimate> stories = new ArrayList<StoryEstimate>();
				for (Story s : dm.getStoriesFromTask(task_id)) {
					stories.add(new StoryEstimate(s, s.getConsensus() == -1 ? 0 : dm.getEstimate(s.getConsensus())
							.getUnitValue()));
				}

				root.put("stories", stories);

				return render("task_summary.ftl", cfg, root);
			}
		});

		get(new Route("/poker/:task_id/:user_id") {
			@Override
			public Object handle(Request request, Response response) {

				Map<String, Object> root = new HashMap<String, Object>();
				int task_id = Integer.parseInt(request.params(":task_id"));
				int user_id = Integer.parseInt(request.params(":user_id"));
				List<Story> storiesFromTask = dm.getStoriesFromTask(task_id);
				List<HashMap<User, List<Estimate>>> story_estimations = new ArrayList<HashMap<User, List<Estimate>>>();
				for (Story s : storiesFromTask) {
					s.setEstimations(dm.getEstimatesFromStory(s.getId()));
					story_estimations.add(dm.getEstimatesFromStory(s.getId()));
				}
				root.put("task", dm.getTask(task_id));
				root.put("stories", storiesFromTask);
				root.put("users", dm.getUsersFromTask(task_id));
				root.put("user", dm.getUser(user_id));
				root.put("estimations", dm.getEstimationsForTask(task_id));
				root.put("story_estimations", story_estimations);

				return render("poker.ftl", cfg, root);
			}
		});

		get(new Route("/task/:task_id/user/:user_id/story/:story_id") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":task_id"));
				int user_id = Integer.parseInt(request.params(":user_id"));
				int story_id = Integer.parseInt(request.params(":story_id"));

				// TODO: Calculate consensus divergence and colour code
				// differences
				List<UserEstimate> latestEstimations = dm.getLatestEstimatesForStory(story_id);
				int iteration = dm.getLatestIteration(story_id);

				List<UserEstimate> previousEstimations = dm.getUserEstimatesForStoryWithIteration(story_id,
						iteration > 0 ? iteration - 1 : iteration);

				if (iteration == 1) {
					System.out.println("debug!");
				}
				
				Gson gson = new GsonBuilder().create();
				HashMap<String, String> gmap = new HashMap<String, String>();

				if (latestEstimations.size() == 0) {
					// sb.append("can vote");
					gmap.put("vote", "true");
				} else {
					boolean userFound = false;
					for (UserEstimate ue : latestEstimations) {
						if (ue.getUser().getId() == user_id) {
							userFound = true;
						}
					}

					if (userFound) {
						gmap.put("vote", "false");
						// sb.append("can not vote");
					} else {
						gmap.put("vote", "true");
						// sb.append("can vote");
					}
				}

				// sb.append(";");

				StringBuilder sb = new StringBuilder();
				// Previous iteration
				if (iteration > 0) {
					sb.append("<span class=\"label label-info\">");

					for (UserEstimate userEstimate : previousEstimations) {

						sb.append(userEstimate.getEstimate().getComplexitySymbol());
						sb.append(" ");
					}
					sb.append("</span>");
				}

				// Next iteration

				boolean consensus = true;
				ArrayList<Integer> values = new ArrayList<Integer>();

				if(iteration == 1) {
					System.out.println("debug!");
				}
				
				for (UserEstimate ue : previousEstimations) {
					values.add(ue.getEstimate().getId());
				}

				for (UserEstimate ue : latestEstimations) {
					sb.append("<button class=\"btn btn-info btn-small\">");
					// sb.append(ue.getEstimate().getComplexitySymbol());
					sb.append("<i class=\"icon-tasks\">");
					sb.append("</i> ");
					sb.append(ue.getUser().getName());
					sb.append("</button>");
				}

				gmap.put("data", sb.toString());

				// calculate consensus

				if (dm.getStory(story_id).getConsensus() == NO_CONSENSUS) {
					System.out.println("Story has no consensus from previous iteration ...");

					if (values.size() == dm.getUsersFromTask(task_id).size()) {
						System.out.println("Everyone have estimated this story ...");
						for (int i = 1; i < values.size(); i++) {
							if (values.get(i - 1) != values.get(i)) {
								consensus = false;
							}
						}
					} else {
						consensus = false;
					}

					if (consensus) {
						System.out.println(String.format("Found consensus for story [%d], inner if", story_id));

						Story s = dm.getStory(story_id);
						if (previousEstimations.size() > 0) {							
							s.setConsensus(previousEstimations.get(1).getEstimate().getId());
							dm.setStory(s);
						}

						// sb.append("done");
						gmap.put("consensus", "true");
					} else {
						System.out.println(String.format("Did not find consensus for story [%d]", story_id));

						// sb.append("not done");
						gmap.put("consensus", "false");
					}
				} else {
					System.out.println(String.format("Found consensus for story [%d]", story_id));
					// sb.append("done");
					gmap.put("consensus", "true");
				}

				System.out.println(gmap);
				return gson.toJson(gmap);
			}
		});

		post(new Route("/task/:task_id/user/:user_id/story/:story_id/ready") {
			@Override
			public Object handle(Request request, Response response) {

				int task_id = Integer.parseInt(request.params(":task_id"));
				int user_id = Integer.parseInt(request.params(":user_id"));
				int story_id = Integer.parseInt(request.params(":story_id"));
				int estimate_id = Integer.parseInt(request.queryParams("estimate_id"));

				List<User> usersOnThisTask = dm.getUsersFromTask(task_id);
				List<UserEstimate> estimatesByUsersThisIteration = dm.getLatestEstimatesForStory(story_id);

				// If everyone has voted, increase iteration
				if (estimatesByUsersThisIteration.size() == usersOnThisTask.size() - 1) {
					dm.addEstimateToStory(story_id, user_id, estimate_id);
					dm.increaseStoryIteration(story_id);
				} else {
					dm.addEstimateToStory(story_id, user_id, estimate_id);
				}

				return "1";
			}
		});

	}
}
