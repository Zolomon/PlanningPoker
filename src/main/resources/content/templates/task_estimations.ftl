<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<div class="row">
	<h1>Estimation settings for ${task.name}:</h1>
	<div class="span12">
	<#escape x as x?html>
		<form class="navbar-form pull-left" method="post" action="/task/<#noescape>${task.id}</#noescape>/edit/estimations">
			<input name="task_id" type="hidden" value="<#noescape>${task.id}</#noescape>">
			<label>Select estimation unit</label>
			<select id="estimation_unit" name="estimation_unit">
			  <option vaule="1">Person Hours</option>
			  <option vaule="2">Person Days</option>
			  <option vaule="3">Person Months</option>
			  <option vaule="4">Person Years</option>
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
					<#escape x as x?html>
					<td><input class="input-mini" type="text" name="complexity-<#noescape>${complexity.id}</#noescape>" placeholder="<#noescape>${complexity.unitValue}</#noescape>"></td>
					</#escape>
				</#list>
				</tr>
			</table>		
			
			<button type="submit" class="btn btn-info">Update</button>
			<a href="/task/<#noescape>${task.id}</#noescape>/edit/stories" class="btn btn-success">Next</a>
		</form>
		</#escape>
	</div>
</div>


</@c.page>