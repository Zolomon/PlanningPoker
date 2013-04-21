<#import "master.ftl" as c/>

<@c.page title="Planning Poker">
<h3>Welcome ${user.name}. Let's play!</h3>
<div class="row">
	<div class="pull-left">
		<table class="table table-condensed table-hover">
			<tr><td colspan="3">&nbsp;</td></tr>
			<#list stories as story>
				<tr>
					<td style="padding: 9px 9px;"><a href="#" id="story-${story.id}">${story.name}</a></td>
					<td>
						<form style="margin: 0;" id="storyform-${story.id}">
							<div class="btn-group" data-toggle="buttons-radio">
								<#list estimations as estimation>
									<button type="button" class="btn btn-primary" id="story-${story.id}-estimate-${estimation.id}" value="${estimation.id}">${estimation.complexitySymbol}</button>
								</#list>
							</div>
						</form>
					</td>
					<td>
						<button class="btn btn-success" disabled id="ready-${story.id}" type="button"><i class="icon-ok-sign icon-white"></i>Ready</button>
					</td>
				</tr>
			</#list>
		</table>			
	</div>
	<div class="pull-left">
		<table class="table table-condensed table-hover">
			<tr>
				<#list users as user>
					<td><a href="#" class="" id="user-${user.id}">${user.name}</a></td>
				</#list>
			</tr>
			<#list stories as story>
				<tr>
					<td colspan="${users?size}"><div id="story-estimations-${story.id}"></div></td>
				</tr>
			</#list>
		</table>			
	</div>
</div>

<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<script src="http://localhost:4567/js/bootstrap.js"></script>

<script type="text/javascript">

	<#list stories as story>
		var timerId${story.id} = 0;
		var img${story.id} = '${story.description}';
		var request${story.id};
		
		
		<#list estimations as estimate>
			$("#story-${story.id}-estimate-${estimate.id}").click(function() {

				var $form = $("#storyform-${story.id}");
				
				if ($form.find("button.active").length > 0) {
					$("#ready-${story.id}").prop("disabled", false);
					console.log("found active button");
				} else {
					$("#ready-${story.id}").prop("disabled", false);
					console.log("didn't find active button");
				}
			});
		</#list>
		
		$("#ready-${story.id}").click(function() {
			// Abort any pending request
			if (request${story.id}) {
				request${story.id}.abort();
			}
			
			// setup some local variables;
			var $form = $("#storyform-${story.id}");
			
			// let's select and cache all the fields
			var $inputs = $form.find("button.active");
			if (!($inputs.length > 0)) {
				return;
			}
			//alert($inputs.attr('value'));
			
			// serialize the data in the form
			var serializedData = $form.serialize();
			
			// let's disable the inputs for the duration of the ajax request
			$form.find("button").prop("disabled", true);
			$("#ready-${story.id}").prop("disabled", true);
			
			// fire off the request
			$.post("/task/${task.id}/user/${user.id}/story/${story.id}/ready", {'estimate_id': $inputs.attr('value')})
			.done(function(data) {
				// We could read the response here instead of during the interval..?
				console.log("from post: " + data);
			});
			 
		});
		
		timerId${story.id} = setInterval(function() {
			$.getJSON("/task/${task.id}/user/${user.id}/story/${story.id}", function(data) {
				//console.log("from get: " + data);
								
				$("#story-estimations-${story.id}").html(data["data"]);
				
				if (data["vote"] === "true") {
					var $inputs = $("#storyform-${story.id}").find("button");
					$inputs.prop("disabled", false);
					
				} else {
					$("#storyform-${story.id}").find("button").prop("disabled", true);
					$inputs.prop("disabled", false);
				}
				
				if (data["consensus"] === "true") {
					$("#storyform-${story.id}").parents("tr").hide();
					$("#story-estimations-${story.id}").parents("tr").hide();					
					clearInterval(timerId${story.id});
					timerId${story.id} = -1;
				}
			});
		}, 500);
		
		setInterval(function() {
			
				if (<#list stories as story> 
						timerId${story.id} === -1 <#if story_has_next>&&</#if> 
					</#list>) {
					window.location.replace("http://localhost:4567/task/${task.id}/summary");
				} 	
			
			
		}, 333);
	</#list>
</script>

</@c.page>