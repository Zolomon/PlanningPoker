<#import "master.ftl" as c/>

<@c.page title="Planning Poker">


<div class="row">
	<div class="span2">
		<table class="table table-condensed table-hover">
			<tr><td>&nbsp;</td></tr>
			<#list stories as story>
				<tr>
					<td style="padding: 9px 9px;"><a href="#" id="story-${story.id}" rel="popover">${story.name}</a></td>
				</tr>
			</#list>
		</table>			
	</div>
	<div class="span3">
		<table class="table table-condensed table-hover">
			<tr>
				<#list users as user>
					<td><a href="#" class="" id="user-${user.id}" rel="popover">${user.name}</a></td>
				</#list>
			</tr>
		</table>			
	</div>
	<div class="pull-right">
		<table class="table table-condensed table-hover">
		<tr><td>&nbsp;</td></tr>
		<#list stories as story>
			<tr>
				<td>
					<div class="btn-group" data-toggle="buttons-radio">
					<#list estimations as estimation>
						<a href="#" type="button" class="btn btn-primary" id="estimate-${estimation.id}" >${estimation.complexitySymbol}</a>
					</#list>
					</div>
				</td>
				<td>
					<button class="btn btn-success" type="button"><i class="icon-ok-sign icon-white"></i>Ready</button>
				</td>
			</tr>
		</#list>
		</table>
	</div>
</div>

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://localhost:4567/js/bootstrap.js"></script>

<script type="text/javascript">
	<#list stories as story>
		var img${story.id} = '${story.description}';

		$("#story-${story.id}").popover({ title: 'Story description', content: img${story.id}, placement: 'left', animation: true, trigger:'hover', delay: {show:333, hide:100} });
	
	</#list>
</script>


</@c.page>