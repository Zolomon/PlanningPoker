<#import "master.ftl" as c/>

<@c.page title="Planning Poker">

<#escape x as x?html>
<div class="row">
	<h1>Stories</h1>
	<div class="span12">
		<table class="table table-condensed table-hover">
		
			<#list stories as story>
				<tr><td><a href="#" id="story-<#noescape>${story.id}</#noescape>" rel="popover">${story.name}</a></td></tr>
			</#list>
		
		</table>
			
	</div>
</div>

	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<script src="http://localhost:4567/js/bootstrap.js"></script>

<script type="text/javascript">

<#list stories as story>
var img${story.id} = '${story.description}';

	$("#story-<#noescape>${story.id}</#noescape>").popover({ title: 'Story description', content: img${story.id}, placement: 'left', animation: true, trigger:'hover', delay: {show:333, hide:100} });

</#list>


</script>


</#escape>
</@c.page>