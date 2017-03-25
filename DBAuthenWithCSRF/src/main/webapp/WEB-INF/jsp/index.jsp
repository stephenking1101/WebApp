<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=utf-8" %>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <meta http-equiv="refresh" content="${pageContext.session.maxInactiveInterval}">

    <link rel="icon" href="<%=request.getContextPath()%>/resources/img/favicon.ico">
    <title>Developer Service</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="<%=request.getContextPath()%>/resources/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/resources/css/index.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="<%=request.getContextPath()%>/resources/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/sjcl.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/index.js"></script>    
  </head>
  <body>
   
    <div class="container">
      <div id="advice" hidden="hidden">
		<div class="alert alert-warning">
		    <strong>提示！</strong>你正在使用旧版本浏览器。 为了能够正常使用本系统所有功能，建议更新你的浏览器。<br>
		    本系统支持Internet Explorer 9或以上, Chrome, Firefox, Safari 以及 Opera 等浏览器。
		</div>
      </div>
      <div id="error">
        <c:if test="${not empty error}">
          <div class="alert alert-danger alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            ${message}
          </div>
        </c:if>
      </div>
      
      <form id="loginForm" class="form-signin" action="<%=request.getContextPath()%>/auth" method="post" onsubmit="openModelDialog()">
        <h2>Please sign in</h2>
        <label for="inputUser" class="sr-only">User Name</label>
        <input type="text" name="username" id="inputUser" class="form-control" placeholder="User id" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me" id="rememberMe"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

    </div> <!-- /container -->
    
    <div class="modal fade" id="progressModal" role="dialog" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="myModalLabel"></h3>
                </div>
                <div class="modal-body">
                    <div class="progress">
                      <div class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                      </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<%=request.getContextPath()%>/resources/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>
