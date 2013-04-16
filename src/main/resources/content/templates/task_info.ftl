<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<div class="span12">
	
		<#if !edit>
			<form class="navbar-form pull-left" method="post" action="/task/new">
		<#else>
			<form class="navbar-form pull-left" method="post" action="/task/${task.id}/edit/info">
		</#if>
			<fieldset>
				<legend>Task Info</legend>
				<label>Task name</label>
				<input type="text" name="task_name" value="${task.name}" placeholder="Enter task name here..." class="span4"><br><br>
				
				<label>Task description</label>
				<textarea  rows="5" name="task_description" placeholder="Enter task description here..." class="span4">${task.description}</textarea><br><br>
				
				<#if !edit>
					<button type="submit" class="btn btn-primary pull-right">Add Task</button>
				<#else>
					<div class="pull-right">
						<button type="submit" class="btn btn-info">Update</button>
						<a href="/task/${task.id}/edit/estimations" class="btn btn-success">Next</a>
					</div>
				</#if>
				
			</fieldset>
		</form>
	</div>
</div>


</@c.page>