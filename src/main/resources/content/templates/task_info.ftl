<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">

	<ul class="breadcrumb">
	<#if !edit>
		<li><a href="#">New task</a></li>
	<#else>
		<li class="active">Info <span class="divider">/</span></li>
		<li><a href="/task/${task.id}/edit/estimations">Estimations</a> <span class="divider">/</span></li>
		<li><a href="/task/${task.id}/edit/stories">Stories</a></li>
	</#if>
	</ul>

</div>

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
						<button type="submit" class="btn btn-info"><i class="icon-hdd icon-white"></i> Update</button>
						<a href="/task/${task.id}/edit/estimations" class="btn btn-success"><i class="icon-circle-arrow-right icon-white"></i> Next</a>
					</div>
				</#if>
				
			</fieldset>
		</form>
	</div>
</div>


</@c.page>