<!doctype html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Hashtag Popularity">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Twitter Hashtag Popularity Snapshot</title>

    <link rel="stylesheet" href="stylesheets/main.css">

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
                    <div class="mdl-grid">
                        <section id="register-form" class="container">
                            <div class="register">
                                <h2>Register</h2>
                                <form method="post" action="register">
                                    <p><input type="text" name="firstName" value="" placeholder="First Name"></p>
                                    <p><input type="text" name="lastName" value="" placeholder="Last Name"></p>
                                    <p><input type="email" name="emailAddress" value="" placeholder="Email Address"></p>
                                    <p><input type="text" name="username" value="" placeholder="Preferred username"></p>
                                    <p><input type="text" name="password" value="" placeholder="Password"></p>
                                    <p class="register-submit"><button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" type="submit" name="register">Register</button></p>
                                </form>
                                <p class="register-cancel"><a href="login"><button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" name="register-cancel">Cancel</button></a></p>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>