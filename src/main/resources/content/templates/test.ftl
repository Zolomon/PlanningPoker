<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">

	<!--<form>
		<input type="text" placeholder="Enter task name">
	</form>-->
	<h1>Tasks</h1>
	<div class="span12">
		<div class="navbar">
			<div class="navbar-inner span3" style="padding: 0 0 6 10;">
				<form class="navbar-form pull-left" method="post" action="/task">
					<input type="text" name="taskname" placeholder="Enter task name here..." class="span2">
					<button type="submit" class="btn">Add Task</button>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="row">
	<h1>Tasks</h1>
	<div class="span12">
		<h3>New Task</h3>
		<p>
			<button class="btn btn-large btn-primary" type="button">New task</button>
		</p>
	</div>
</div>


</@c.page>