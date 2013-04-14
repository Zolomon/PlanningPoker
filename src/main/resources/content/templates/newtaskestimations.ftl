<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Estimation settings for new task: ${taskname}</h1>
	<div class="span12">
		<form class="navbar-form pull-left" method="post" action="/tasks/new/estimation">
			<input type="text" name="estimationunit" placeholder="Enter estimate unit here..." class="span2"><br><br>
			<input type="text" name="estimationunitvalue" placeholder="Enter estimate unit value here..." class="span2"><br><br>
			<button type="submit" class="btn">Next</button>
		</form>
	</div>
</div>


</@c.page>