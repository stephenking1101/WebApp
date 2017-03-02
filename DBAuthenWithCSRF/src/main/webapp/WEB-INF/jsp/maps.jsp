<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=utf-8" %> 
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
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap-table.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/resources/css/chartist.min.css" rel="stylesheet">
    
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap-table.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootstrap-table-en-US.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/bootbox.min.js"></script>
    <!--  Charts Plugin -->
    <script src="<%=request.getContextPath()%>/resources/js/chartist.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/handlebars.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/home.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=BfdoZsVVwpFP2T27qxfkhAvavjI6wEVp"></script>
    <script>
            function initMap() {  
            	$("#allmap").show();
                var map = new BMap.Map("allmap");    // 创建Map实例
                map.centerAndZoom(new BMap.Point(113.275, 23.117), 12);  // 初始化地图,设置中心点坐标和地图级别
                map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
                map.setCurrentCity("广州");          // 设置地图显示的城市 此项是必须设置的
                map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            }
    </script>
    <style type="text/css">
            #allmap {width: 100%;height: 500px;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
    <!-- <style>
	.btn-default:focus,
	.btn-default.focus {
	  color: #333;
	  background-color: #fff;
	  border-color: #ccc;
	}
    </style>  -->
    <style>
    /* CUSTOMIZE THE CAROUSEL
	-------------------------------------------------- */
	
	/* Carousel base class */
	.carousel {
	  height: 500px;
	  margin-bottom: 60px;
	}
	/* Since positioning the image, we need to help out the caption */
	.carousel-caption {
	  z-index: 10;
	}
	
	/* Declare heights because of positioning of img element */
	.carousel .item {
	  height: 500px;
	  background-color: #777;
	}
	.carousel-inner > .item > img {
	  position: absolute;
	  top: 0;
	  left: 0;
	  min-width: 100%;
	  height: 500px;
	}
	@media (min-width: 768px) {
	  /* Bump up size of carousel content */
	  .carousel-caption p {
	    margin-bottom: 20px;
	    font-size: 21px;
	    line-height: 1.4;
	  }
	}
    </style> 
    <sec:authentication property="authorities" var="authorities"/>
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
            <li><a id="maps" href="javascript:window.history.back();">&#171;BACK</a></li>
          </ul>
		  <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${username} <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#" value='<c:forEach items="${authorities}" var="authority">${authority.authority},</c:forEach>' onclick="showPermission(event);">Permissions</a></li>
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
      
      </div>
      <div id="allmap" hidden="hidden"></div>
    </div>

    <footer class="footer">
        <div class="container">
            <p class="text-muted pull-right">
                &copy; 2016 <a href="#">Superware</a>, made with love for a better web
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
