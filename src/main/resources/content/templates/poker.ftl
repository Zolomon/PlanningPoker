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
		var img${story.id} = '${story.description}';
		var request${story.id};
		
		//$("#story-${story.id}").popover({ title: 'Story description', content: img${story.id}, placement: 'left', animation: true, trigger:'hover', delay: {show:333, hide:100} });
		
		for(var i = 0; i < ${estimations?size}; i++) {
			$("#story-${story.id}-estimate-" + i).click(function() {
				/*if($("#story-${story.id}-estimate-" + i).hasClass("active")) {
					//$("#ready-${story.id}").addClass("disabled");
				} else if (!$("#story-${story.id}-estimate-" + i).hasClass("active")) {
					$("#story-${story.id}-estimate-" + i).addClass("active");
					//$("#ready-${story.id}").removeClass("disabled");
				}*/
				

				var $form = $("#storyform-${story.id}");
				
				if ($form.find("button.active").length > 0) {
					$("#ready-${story.id}").prop("disabled", false);
				} else {
					$("#ready-${story.id}").prop("disabled", false);
				}
								
			});
		}
		
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
		
		setInterval(function() {
			$.get("/task/${task.id}/user/${user.id}/story/${story.id}", function(data) {
				console.log("from get: " + data);
				var substr = data.split(';');
				$("#story-estimations-${story.id}").html(substr[1]);
				if (substr[0] === "can vote") {
					var $inputs = $("#storyform-${story.id}").find("button");
					$inputs.prop("disabled", false);
				}
				//if (data === "can vote") {
				//	$inputs.prop("disabled", false);	
				//}
			});
		}, 3000);
	</#list>
</script>

</@c.page>