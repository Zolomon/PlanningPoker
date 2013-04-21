<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<div class="span12">
	<legend>Tasks</legend>
		<table class="table table-condensed table-hover">
			<thead><tr><th>Summary</th><th>Story name</th><th>Actions</th></tr></thead>
			<#list tasks as task>
			<tr>
			<#escape x as x?html>
				<td class="span2">
					<a href="/task/<#noescape>${task.id}</#noescape>/summary" class="btn btn-mini btn-info"><i class="icon-info-sign icon-white"></i> Summary</a>
				</td>
				<td>
					<a href="/task/<#noescape>${task.id}</#noescape>/edit/info" id="task-title-${task.id}">${task.name}</a>
					
					<#if !task.isPublished()>
						<small>(unpublished)</small>
					</#if>
				</td>
				<td>
					<a class="btn btn-danger btn-mini pull-right" href="/task/<#noescape>${task.id}</#noescape>/delete/"><i class="icon-trash icon-white"></i> Delete</a>
				</td>
			</tr>
			
			<tr>
				<td><small>User links:</small></td>
				<td colspan="2">
				<#if task.isPublished()>
					<#list task.users as user>
						<a class="btn btn-primary btn-mini" href="/poker/<#noescape>${task.id}</#noescape>/<#noescape>${user.id}</#noescape>"><i class="icon-play icon-white"></i>&nbsp;&nbsp;<strong>${user.name}</strong></a>
					</#list>
				</td>
				</#if>
			</tr>
			
			<tr><td colspan="3">&nbsp;</td></tr>
			</#escape> 
			</#list>
		</table>
			<form class="navbar-form pull-left" method="get" action="/task/new">
				<p>
					<button class="btn btn-large btn-primary" type="submit"><i class="icon-pencil icon-white"></i> New task</button>
				</p>
			</form>
	</div>
</div>

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://localhost:4567/js/bootstrap.js"></script>
<script type="text/javascript">
	<#list tasks as task>
		var img${task.id} = '${task.description}';

		$("#task-title-${task.id}").popover({ title: 'Task Description', content: img${task.id}, placement: 'right', animation: true, trigger:'hover', delay: {show:333, hide:100} });
	
	</#list>
</script>

</@c.page>