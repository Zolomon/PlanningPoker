<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>New Task</h1>
	<div class="span12">
		<form class="navbar-form pull-left" method="post" action="/task/new/estimation">
			<input type="text" name="taskname" placeholder="Enter task name here..." class="span2"><br><br>
			<textarea name="taskdescription" placeholder="Enter task description here..." class="span2"></textarea><br><br>
			<button type="submit" class="btn">Add Task</button>
		</form>
	</div>
</div>


</@c.page>