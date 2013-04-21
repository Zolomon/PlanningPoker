<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<legend>Task Summary</legend>
	<form class="navbar-form pull-left" method="post" action="/task/${id}/summary">
				<label>Select estimation unit</label>
				<select id="estimation_unit" name="estimation_unit">
				  <option value="1" <#if unit_id == 1>selected</#if>>Person Hours</option>
				  <option value="2" <#if unit_id == 2>selected</#if>>Person Days</option>
				  <option value="3" <#if unit_id == 3>selected</#if>>Person Months</option>
				  <option value="4" <#if unit_id == 4>selected</#if>>Person Years</option>
				</select>
				<button type="submit" class="inline btn btn-primary" style="margin-top: 6px;"><i class="icon-refresh icon-white"></i> Change Unit</button>
		</form>
</div>
<br />
<div class="row">		
	<div class="span12">
		<table class="table table-condensed table-hover">
		<thead><tr><th>Name</th><th>Description</th><th>Consensus</th><th>Value
		
		<#if unit_id == 1>
			(Person Hours)
		<#elseif unit_id == 2>
			(Person Days)
		<#elseif unit_id == 3>
			(Person Months)
		<#elseif unit_id == 4>
			(Person Years)
		</#if>
		
		</th></tr></thead>
		<#list stories as story>
			<tr>
				<td>${story.story.name}</td>
				<td>${story.story.description}</td>
				<td>${story.complexityString}</td>
				<td>
					<#if story.estimate != "-1">
						${story.estimate}
					</#if>
				</td>
			</tr>
		</#list>
		<tr>
			<td colspan=3><strong>Total: </strong></td>
			<td><span class="label label-info">${total}</span></td>
		</tr>
		</table>
	</div>
</div>


</@c.page>