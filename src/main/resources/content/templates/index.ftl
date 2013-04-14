<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Tasks</h1>
	<div class="span12">
		<table class="table table-condensed table-hover">
			<#list tasks as task>
			<tr>
			<#escape x as x?html>
				<td>
				<a href="/tasks/edit/info/<#noescape>${task.id}</#noescape>">${task.name}</a></td>
			</#escape>
			</tr>
			</#list>
		</table>
			<form class="navbar-form pull-left" method="get" action="/new">
				<p>
					<button class="btn btn-large btn-primary" type="submit">New task</button>
				</p>
			</form>
	</div>
</div>


</@c.page>