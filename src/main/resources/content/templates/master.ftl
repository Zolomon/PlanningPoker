<#macro page title>

	<html>
	
	<head>
		<title>${title?html}</title>
	</head>
	
	<body>
	
	<!-- header section -->
	
	<link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
    <link href="css/docs.css" rel="stylesheet">
    <link href="js/google-code-prettify/prettify.css" rel="stylesheet">
	
	
	<#nested/>
	
	<!-- footer section -->
	
	</body>
	
	</html>

</#macro>