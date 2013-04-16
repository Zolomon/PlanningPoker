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
					<a href="/task/<#noescape>${task.id}</#noescape>/edit/info/">${task.name}</a>
				</td>
				<td>
					<a class="btn btn-danger btn-mini pull-right" href="/task/<#noescape>${task.id}</#noescape>/delete/"><i class="icon-trash"></i> Delete</a>
				</td>
			</tr>
			
			<tr>
				<td colspan="3">
					<#list task.users as user>
						<a class="btn btn-primary btn-mini" href="/poker/<#noescape>${task.id}</#noescape>/<#noescape>${user.id}</#noescape>"><i class="icon-play icon-black"></i>&nbsp;&nbsp;${user.name}</a>
					</#list>
				</td>
			</tr>
			
			<tr><td colspan="3">&nbsp;</td></tr>
			</#escape> 
			</#list>
		</table>
			<form class="navbar-form pull-left" method="get" action="/task/new">
				<p>
					<button class="btn btn-large btn-primary" type="submit">New task</button>
				</p>
			</form>
	</div>
</div>


</@c.page>