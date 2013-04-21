<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">

	<ul class="breadcrumb">
		<li><a href="/task/${task.id}/edit/info">Info</a> <span class="divider">/</span></li>
		<li><a href="/task/${task.id}/edit/estimations">Estimations</a> <span class="divider">/</span></li>
		<li class="active">Stories</li>
	</ul>

</div>


<#escape x as x?html>
<div class="row">
	<div class="span12">
		<form id="add_story" class="navbar-form pull-left" method="post" action="/task/<#noescape>${task.id}</#noescape>/story/add">
		<legend>Add a new stories</legend>
			<label>Story Name</label>
			<input class="span4" type="text" name="story_name" placeholder="Enter story name here..."><br><br>
			<textarea class="span4" rows="5" name="story_description" placeholder="Enter story description here..." class="span4"></textarea><br><br>
			<button type=submit" name="action" value="add_story" class="btn pull-right"><i class="icon-pencil"></i> Add story</button>
			<br><br>
		</form>
	</div>
</div>
<div class="row">
	<div class="span12">
		<legend>Stories</legend>
		<table class="table table-condensed table-hover">
			<thead><tr><th>Story name</th><th>Actions</th></tr></thead>
			<#list stories as story>
				<tr>
					<td><a href="#" id="story-<#noescape>${story.id}</#noescape>" rel="popover">${story.name}</a></td>
					<td>
						<a class="btn btn-danger btn-mini pull-right" href="/task/<#noescape>${task.id}</#noescape>/story/<#noescape>${story.id}</#noescape>/delete">
							<i class="icon-trash icon-white"></i> Delete
						</a>
					</td>
				</tr>
			</#list>
		</table>
	</div>	
</div>	
<div class="row">
	<div class="span12">
		<div class="pull-left">
		<a href="/task/<#noescape>${task.id}</#noescape>/edit/estimations" class="btn"><i class="icon-circle-arrow-left icon-black"></i> Back</a> 
		</div>	
		<div class="pull-right">
			<#if !published>
			<form id="publish_task" class="navbar-form pull-left" method="post"  action="/task/<#noescape>${task.id}</#noescape>/publish">
				<button type="submit" name="action" value="publish_task" class="btn btn-success"><i class="icon-ok icon-white"></i> Publish task</button>
			</form>
			<#else>
			<form id="publish_task" class="navbar-form pull-left" method="post"  action="/task/<#noescape>${task.id}</#noescape>/unpublish">
				<button type="submit" name="action" value="publish_task" class="btn btn-warning"><i class="icon-remove icon-white"></i> Unpublish task</button>
			</form>
			</#if>
		</div>
	</div>
</div>

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://localhost:4567/js/bootstrap.js"></script>

<script type="text/javascript">
	<#list stories as story>
		var img${story.id} = '${story.description}';

		$("#story-<#noescape>${story.id}</#noescape>").popover({ title: 'Story Description', content: img${story.id}, placement: 'right', animation: true, trigger:'hover', delay: {show:333, hide:100} });
	
	</#list>
</script>
</#escape>


</@c.page>