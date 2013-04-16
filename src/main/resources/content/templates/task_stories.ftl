<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Stories for new task:</h1>
	<div class="span12">
	<#escape x as x?html>
		<form class="navbar-form pull-left" method="post"  action="/task/<#noescape>${task.id}</#noescape>/edit/stories">
			<span class="help-inline">Story Name: </span><input type="text" name="storyname1">
			<span class="help-inline">Description: </span><input type="text" name="storydescription1"><br><br>
			<button type="button" class="btn">Add another story</button><br><br>
			<button type="submit" class="btn">Next</button>
		</form>
	</#escape>
	</div>
</div>


</@c.page>