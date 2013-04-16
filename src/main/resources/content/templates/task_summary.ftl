<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Task Summary</h1>
	<div class="span12">
		<table class="table table-condensed table-hover">
		<thead><tr><th>Name</th><th>Description</th><th>Consensus</th></tr></thead>
		<#list stories as story>
			<tr>
				<td>${story.name}</td>
				<td>${story.description}</td>
				<td>${story.consensus}</td>
			</tr>
		</#list>
		</table>
	</div>
</div>


</@c.page>