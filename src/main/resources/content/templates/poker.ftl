<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Tasks</h1>
	<div class="span12">
		<table class="table table-condensed table-hover">
			<#list tasks as task>
			<tr>
			<#escape x as x?html>
				<td class="span1">
					<a href="/summary/<#noescape>${task.id}</#noescape>" class="btn btn-mini btn-info">Summary</a>
				</td>
				<td>
					<a href="/tasks/edit/info/<#noescape>${task.id}</#noescape>">${task.name}</a>
				</td>
			
			</tr>
			
			<#list task.users as user>
			<tr>
					<td colspan="2"><a href="/poker/<#noescape>${task.id}</#noescape>/<#noescape>${user.id}</#noescape>">${user.name}</a></td>
			</tr>
			</#list>
			<tr><td colspan="2">&nbsp;</td></tr>
			</#escape> 
			</#list>
		</table>
			<form class="navbar-form pull-left" method="get" action="/tasks/new">
				<p>
					<button class="btn btn-large btn-primary" type="submit">New task</button>
				</p>
			</form>
	</div>
</div>


</@c.page>