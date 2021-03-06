<#macro page title>
<!DOCTYPE html>
<html lang="en">	
	<head>
		<title>${title?html}</title>
		
		<link href="http://${ip}:${port?c}/css/bootstrap.css" rel="stylesheet">
	    <link href="http://${ip}:${port?c}/css/bootstrap-responsive.css" rel="stylesheet">
	    <link href="http://${ip}:${port?c}/css/docs.css" rel="stylesheet">
	    <link href="http://${ip}:${port?c}/js/google-code-prettify/prettify.css" rel="stylesheet">
	    <link href="http://${ip}:${port?c}/img/favicon.png" rel="shortcut icon" type="image/x-icon" >
		<style>
			body {
				padding-top: 80px;
			}
		</style>
	</head>
	
	
	<body>
	
	<!-- header section -->
	
	
	<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="/">Planning Poker</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="/">Tasks</a></li>
              <li><a href="https://github.com/Zolomon/PlanningPoker/">GitHub</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

	
	
	<div class="container">
		<#nested/>
		
		<br />
		<br />
		<br />
		<div class="row">
			<div class="span12">
					<p class="text-center muted"><small>Created by <strong>Soheil Afghani Khorasgani</strong>, <strong>Bengt Ericsson</strong>, <strong>Anders Holmberg</strong>, <strong>Daniel Lundell</strong>, <strong>Alexander Pieta Theofanous</strong>.</small></p>   
			</div>
		</div>
	</div><!--/.container-->
	
	
	<!-- footer section -->
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<script src="http://localhost:4567/js/bootstrap.js"></script>
	</body>
	
	</html>

</#macro>
