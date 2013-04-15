<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Estimation settings for ${task.name}:</h1>
	<div class="span12">
		<form class="navbar-form pull-left" method="post" action="/tasks/edit/estimationsettings/process">
			<input name="taskid" type="hidden" value="${task.id}">
			<select name="estimationunit">
			  <option value="0">Select estimation unit</option>
			  <option vaule="1">Person hour</option>
			  <option vaule="2">Person Days</option>
			  <option vaule="3">Person Months</option>
			  <option vaule="4">Person Days</option>
			</select><br><br>
			<p>Enter value for one estimation unit:</p>
			<input type="text" name="estimationunitvalue" placeholder="Enter man hour(s)" class="span2"><br><br>
			<button type="submit" class="btn">Next</button>
		</form>
	</div>
</div>


</@c.page>