<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<ul class="breadcrumb" style="margin-bottom: 5px;">
		<li><a href="/task/${task.id}/edit/info">Info</a> <span class="divider">/</span></li>
		<li class="active">Estimations <span class="divider">/</span></li>
		<li><a href="/task/${task.id}/edit/stories">Stories</a></li>
	</ul>
</div>

<div class="row">
	<h1>Estimation settings for ${task.name}:</h1>
	<div class="span12">
	<#escape x as x?html>
		<form class="navbar-form pull-left" method="post" action="/task/<#noescape>${task.id}</#noescape>/edit/estimations">
			<input name="task_id" type="hidden" value="<#noescape>${task.id}</#noescape>">
			<label>Select estimation unit</label>
			<select id="estimation_unit" name="estimation_unit">
			  <option value="1" <#if unit_id == 1>selected</#if>>Person Hours</option>
			  <option value="2" <#if unit_id == 2>selected</#if>>Person Days</option>
			  <option value="3" <#if unit_id == 3>selected</#if>>Person Months</option>
			  <option value="4" <#if unit_id == 4>selected</#if>>Person Years</option>
			</select>
				<br/><br/>
			<label>Assign the worth of each complexity value</label>
			<table class="table table-condensed table-hover pull-left">
				<tr>
				<#list complexities as complexity>
					<td><strong>${complexity.complexitySymbol}</strong></td>
				</#list>
				</tr>
				<tr>
				<#list complexities as complexity>
					<td><input class="input-mini" type="text" name="complexity-${complexity.id}" placeholder="${complexity.unitValue}"></td>
				</#list>
				</tr>
			</table>		
			
			<a href="/task/<#noescape>${task.id}</#noescape>/edit/info" class="btn pull-left"><i class="icon-circle-arrow-left icon-black"></i> Back</a>
			
			<div class="pull-right">
				<button type="submit" class="btn btn-info"><i class="icon-hdd icon-white"></i> Update</button>
				<a href="/task/<#noescape>${task.id}</#noescape>/edit/stories" class="btn btn-success"><i class="icon-circle-arrow-right icon-white"></i> Next</a>
			</div>
		</form>
		</#escape>
	</div>
</div>


</@c.page>