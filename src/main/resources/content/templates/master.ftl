<#macro page title>

	<html>
	
	<head>
		<title>${title?html}</title>
		
		<link href="http://localhost:4567/css/bootstrap.css" rel="stylesheet">
	    <link href="http://localhost:4567/css/bootstrap-responsive.css" rel="stylesheet">
	    <link href="http://localhost:4567/css/docs.css" rel="stylesheet">
	    <link href="http://localhost:4567/js/google-code-prettify/prettify.css" rel="stylesheet">
	    <!--<link rel="shortcut icon" href="img/favicon.png">-->
	    <link rel="shortcut icon" type="image/x-icon" href="http://localhost:4567/img/favicon.png">
	</head>
	
	<style>
	
	body {
		padding-top: 80px;
	}
		
	</style>
	
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
              <li><a href="#about">Your Story</a></li>
              <li><a href="https://github.com/Zolomon/PlanningPoker/">GitHub</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

	
	
	<div class="container">
		<#nested/>
	</div><!--/.container-->
	
	<!-- footer section -->
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<script src="http://localhost:4567/js/bootstrap.js"></script>
	</body>
	
	</html>

</#macro>