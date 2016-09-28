<!doctype html>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Hashtag Popularity">
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
                    <h3><c:out value="${sessionScope.user.fName}"/> <c:out value="${sessionScope.user.lName}"/></h3>
					<section>
						<div class="mdl-grid">
  							<div class="mdl-cell mdl-cell--4-col">
  								<p><b>Create Snapshot:</b></p>
  								<form method="post" action="snapshot">
                                    <p>#<input type="text" name="hashtag0" value="" placeholder="Add hashtag"></p>
                                    <p>#<input type="text" name="hashtag1" value="" placeholder="Add hashtag"></p>
                                    <p>#<input type="text" name="hashtag2" value="" placeholder="Add hashtag"></p>
                                    <p>#<input type="text" name="hashtag3" value="" placeholder="Add hashtag"></p>
                                    <p>#<input type="text" name="hashtag4" value="" placeholder="Add hashtag"></p>
                                    <p>&nbsp;<input type="text" name="interval" value="" placeholder="Choose interval">&nbsp;seconds</p>
                                    <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" type="submit" name="submit" value="save">Save to Favorites</button>
                                    <br><br>
 									<button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" type="submit" name="submit" value="view">View Snapshot Graph</button>
                                </form>
  							</div>
  							
  							<div class="mdl-cell mdl-cell--8-col">
								<p><b>Favorites:</b></p>
								<form method="post" action="snapshot">
									<ul>
										<c:forEach items="${favorites}" var="snapshot" varStatus="i">
												<li>
													<button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" type="submit" name="submit" value=${i.index}>Select</button>
													<b>Hashtags</b>:&nbsp;
													<c:forEach items="${snapshot.hashtags}" var="hashtag">
														<c:out value="#${hashtag}, "/>
													</c:forEach>
													<b>Interval</b>:&nbsp;
													<c:out value="${snapshot.interval} seconds"/>
												</li>
												<br>
										</c:forEach>
									</ul>
								</form>
							</div>
						</div>
					</section>
                </div>
            </div>
        </main>
    </div>
</body>
</html>