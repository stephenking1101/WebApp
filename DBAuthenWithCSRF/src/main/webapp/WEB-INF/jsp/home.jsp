<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="_csrf_parameterName" content="${_csrf.parameterName}"/>
    <link rel="icon" href="<%=request.getContextPath()%>/resources/img/favicon.ico">

    <title>Developer Service Tool</title>

    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="<%=request.getContextPath()%>/resources/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/resources/css/home.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="<%=request.getContextPath()%>/resources/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script> -->
    <script>window.jQuery || document.write('<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"><\/script>')</script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap.js"></script>
    <!--  Charts Plugin -->
    <script src="<%=request.getContextPath()%>/resources/js/chartist.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/handlebars.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/home.js"></script>
    <!-- <style>
	.btn-default:focus,
	.btn-default.focus {
	  color: #333;
	  background-color: #fff;
	  border-color: #ccc;
	}
    </style>  -->
  </head>

  <body>

    <!-- Static navbar -->
    <nav class="navbar navbar-inverse navbar-static-top">
      <div class="container">
        <input type="hidden" id="currentMenu" value="${currentMenu}">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Tools</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a id="home" href="<%=request.getContextPath()%>/home">HOME</a></li>
            <sec:authorize access="hasAnyRole('ROLE_POWERUSER', 'ROLE_USER')">
            <li><a id="file" href="#">FILE</a></li>
            </sec:authorize>
            <!-- 
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">DROPDOWN <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li role="separator" class="divider"></li>
                <li class="dropdown-header">Header</li>
                <li><a href="#">Separated link</a></li>
              </ul>
            </li>
             -->
            <li><a href="#">ABOUT</a></li>
            <li><a href="#">CONTACT</a></li>
          </ul>
		  <ul class="nav navbar-nav navbar-right">
		    <sec:authorize access="hasRole('ROLE_ADMIN')">
		    <li><a href="#" data-toggle="modal" data-target="#login-modal">ADMIN</a></li>
		    </sec:authorize>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${username} <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Permissions</a></li>
                <li role="separator" class="divider"></li>
                <li><a href="<%=request.getContextPath()%>/logout">Logout</a></li>
              </ul>
            </li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <!-- Begin page content -->
    <div class="container">
      <div id="content">
      <div class="page-header">
        <h1>Welcome ${username}</h1>
      </div>
      <p class="lead">Pin a fixed-height footer to the bottom of the viewport in desktop browsers with this custom HTML and CSS. A fixed navbar has been added with <code>padding-top: 60px;</code> on the <code>body > .container</code>.</p>
      <p>Back to <a href="#">the default sticky footer</a> minus the navbar.</p>
      </div>
    </div>

    <footer class="footer">
        <div class="container">
            <p class="text-muted pull-right">
                &copy; 2016 <a href="#">Supperware</a>, made with love for a better web
            </p>
        </div>
    </footer>
    
    <div class="modal fade" id="progressModal" role="dialog" tabindex="-1" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <h3 id="myModalLabel"></h3>
                </div>
                <div class="modal-body">
                    <div class="progress">
                      <div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                      </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <!-- close button 
		            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		                  &times;            
		            </button>  -->
		            <h4 class="modal-title" id="errorModalLabel"></h4>
		        </div>
		        <div class="modal-body" id="errorModalBody"></div>
		        <div class="modal-footer">
		            <!-- <button type="button" class="btn btn-default" 
		               data-dismiss="modal">关闭            </button> -->
		            <button type="button" class="btn btn-primary" id="errorModalButton">OK</button>
		        </div>
		     </div><!-- /.modal-content -->
		 </div><!-- /.modal -->
    </div>
    <div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="adminModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="background-color:#5cb85c;text-align:center;padding:35px 50px;">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h1><span class="glyphicon glyphicon-lock"></span>Login to Admin Page</h1>
                </div>
                <div class="modal-body" style="padding:40px 50px;">
                  <div id="adminError" class="alert alert-danger alert-dismissible" role="alert">
			            
			      </div>
                  <form role="form" action="/admin" method="POST">
                    <input type="text" id="user" name="user" class="form-control" placeholder="Username" value="${username}" required disabled>
                    <input type="password" id="pass" name="pass" class="form-control" placeholder="Password" required autofocus>
                    <button type="button" id="loginButton" name="login" class="btn btn-success btn-block">Login</button>
                  </form>
                </div>
            </div>
         </div>
    </div>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<%=request.getContextPath()%>/resources/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>
