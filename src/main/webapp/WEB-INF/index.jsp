<!doctype html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Hashtag Popularity Snapshot">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Twitter Hashtag Popularity Snapshot</title>

    <link rel="stylesheet" type="text/css" href="stylesheets/main.css">

    <link href="https://fonts.googleapis.com/css?family=Roboto:regular,bold,italic,thin,light,bolditalic,black,medium&amp;lang=en" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <link rel="stylesheet" href="https://storage.googleapis.com/code.getmdl.io/1.0.6/material.blue-indigo.min.css" />
    <link rel="stylesheet" href="http://www.getmdl.io/templates/article/styles.css">
    <script src="https://storage.googleapis.com/code.getmdl.io/1.0.2/material.min.js"></script>

</head>
<body class="mdl-demo mdl-color--grey-100 mdl-color-text--grey-700 mdl-base">
<div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
    <header class="demo-header mdl-layout__header mdl-layout__header--scroll mdl-color--grey-100 mdl-color-text--grey-800">
        <div class="mdl-layout__header-row">
            <span class="mdl-layout-title"><a href="${pageContext.request.contextPath}/" style="color: inherit; text-decoration: none">Twitter Hashtag Popularity Snapshot</a></span>
            <div class="mdl-layout-spacer"></div>
            <nav class="mdl-navigation">
       			<a class="mdl-navigation__link mdl-color-text--grey-800" href="snapshot">Snapshot</a>
        		<a class="mdl-navigation__link mdl-color-text--grey-800" href="login">Login</a>
     		 </nav>
        </div>
    </header>
	
    <div class="demo-ribbon"></div>
	<main class="demo-main mdl-layout__content">
            <div class="demo-container mdl-grid">
                <div class="mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"></div>
                <div class="demo-content mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col">
					  <h3>Twitter Hashtag Popularity Snapshot</h3> 
					  <p>This simple application allows you to track the relative popularity of a set of Twitter hashtags in real time. 
					  	In building your "snapshot" you can specify up to 5 hashtags to track and select a time interval (in seconds).
					  	The interval refers to the length of one datapoint for a given snapshot. So, if you choose a 5 second interval
					  	the app will count up the uses of your hashtags for each 5 second interval while the app is running. You'll see
					  	a new datapoint show up on the graph roughly every 5 seconds. 
					  	Here's how to use the app:</p>
					  <ol>
					  	<li>Register an account</li>
					  	<li>Login</li>
					  	<li>Select you hashtags and interval</li>
					  	<li>Save the snapshot as a favorite.</li>
					  	<li>Select a favorite snapshot to view the graph</li>
					  </ol>
                      <ul>
                      	<li><a href="login">Login</a></li>
                      	<li><a href="register">Register</a></li>
                      </ul>
                </div>
            </div>
        </main>
    </div>
</body>
</html>