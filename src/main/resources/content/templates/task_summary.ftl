<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<legend>Task Summary</legend>
	<form class="navbar-form pull-left" method="post" action="/task/${task.id}/summary">
			<label>Select estimation unit</label>
			<select id="estimation_unit" name="estimation_unit">
			  <option vaule="1">Person Hours</option>
			  <option vaule="2">Person Days</option>
			  <option vaule="3">Person Months</option>
			  <option vaule="4">Person Years</option>
			</select>
			<div class="pull-right">
				<button type="submit" class="btn btn-primary btn-small"><i class="icon-hdd icon-white"></i> Change Unit</button>
			</div>
		</form>
	<div class="span12">
		<table class="table table-condensed table-hover">
		<thead><tr><th>Name</th><th>Description</th><th>Consensus</th><th>Value
		
		<#if unit_id == 1>
			(man-hours)
		<#elseif unit_id == 2>
			(man-days)
		<#elseif unit_id == 3>
			(man-months)
		<#elseif unit_id == 4>
			(man-years)
		</#if>
		
		</th></tr></thead>
		<#list stories as story>
			<tr>
				<td>${story.story.name}</td>
				<td>${story.story.description}</td>
				<td>${story.complexityString}</td>
				<td>
				
				<#if story.estimate != 0>
				
				${story.estimate}
				
				</#if>
				
				
				</td>
			</tr>
		</#list>
		</table>
	</div>
</div>


</@c.page>