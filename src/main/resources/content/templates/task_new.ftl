<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>New Task</h1>
	<div class="span12">
		<form class="navbar-form pull-left" method="post" action="/task/new">
			<fieldset>
				<label for="taskname">Task name</label>
				<input type="text" name="task_name" placeholder="Enter task name here..." class="span4"><br><br>
				
				<label>Task description</label>
				<textarea  rows="5" name="taskd_escription" placeholder="Enter task description here..." class="span4"></textarea><br><br>
				<button type="submit" class="btn btn-primary pull-right">Add Task</button>
			</fieldset>
		</form>
	</div>
</div>


</@c.page>