<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<link rel="icon" type="image/png" href="<%=request.getContextPath()%>/resources/img/favicon.ico">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

	<title>Admin</title>

	<meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
	<meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="_csrf_parameterName" content="${_csrf.parameterName}"/>

    <link href="<%=request.getContextPath()%>/resources/css/jquery-ui.min.css" rel="stylesheet" />
    <!-- Bootstrap core CSS     -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" />
    <!-- Bootstrap table CSS -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap-table.min.css" rel="stylesheet" />
    
    <!-- Animation library for notifications   -->
    <link href="<%=request.getContextPath()%>/resources/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="<%=request.getContextPath()%>/resources/css/light-bootstrap-dashboard.css" rel="stylesheet"/>

    <link href="<%=request.getContextPath()%>/resources/css/admin.css" rel="stylesheet" />

    <!--     Fonts and icons     -->
    <link href="<%=request.getContextPath()%>/resources/css/pe-icon-7-stroke.css" rel="stylesheet" />
    <style>
    .table-hover > tbody > tr:hover {
		  background-color: #dff0d8;
		}
		
	.no-close .ui-dialog-titlebar-close {
	  display: none;
	}
	</style>
</head>
<body>
<sec:authorize access="hasRole('ROLE_ADMIN')">
<div class="wrapper">
    <div class="sidebar" data-color="green" data-image="<%=request.getContextPath()%>/resources/img/sidebar-2.jpg">
    <input type="hidden" id="currentMenu" value="${currentMenu}">
    <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


    	<div class="sidebar-wrapper">
            <div class="logo">
                <a href="#" class="simple-text">
                    Admin Page
                </a>
            </div>

            <ul class="nav">
                <li class="active">
                    <a id="admin/users" href="#">
                        <i class="pe-7s-note2"></i>
                        User List
                    </a>
                </li>
                <li>
                    <a id="admin/maps" href="#">
                        <i class="pe-7s-map-marker"></i>
                        Maps
                    </a>
                </li>
				<!-- 
				<li class="active-pro">
                    <a href="#">
                        <i class="pe-7s-rocket"></i>
                        Return To Home
                    </a>
                </li> -->
            </ul>
    	</div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default navbar-fixed">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#"></a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${username}
                                    <b class="caret"></b>
                              </a>
                              <ul class="dropdown-menu">
                                <li><a href="#">Account</a></li>
                                <li class="divider"></li>
                                <li><a href="<%=request.getContextPath()%>/logout">Log out</a></li>
                              </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>


        <div class="content">
            <div class="container-fluid">
            <div id="content">
            </div>
            </div>
        </div>

        <footer class="footer">
            <div class="container-fluid">
                <nav class="pull-left">
                    <ul>
                        <li>
                            <a href="<%=request.getContextPath()%>/home">
                                Home
                            </a>
                        </li>
                        <!-- 
                        <li>
                            <a href="#">
                                Company
                            </a>
                        </li> -->
                    </ul>
                </nav>
                <p class="copyright pull-right">
                    &copy; 2016 <a href="#">Supperware</a>, made with love for a better web
                </p>
            </div>
        </footer>
    </div>
    
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
                       data-dismiss="modal">Close            </button> -->
                    <button type="button" class="btn btn-primary" id="errorModalButton">OK</button>
                </div>
             </div><!-- /.modal-content -->
         </div><!-- /.modal -->
    </div>
    
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" style="background-color:#1DC7EA;">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" id="editModalLabel">Edit User</h4>
                </div>
                <div class="modal-body">
                  <div id="editError" class="alert alert-danger alert-dismissible" role="alert" hidden="hidden">
                        
                  </div>
                  <div class="form-group" id="newNameDiv">
                    <label>User Name</label>
                    <input type="text" id="newName" name="newName" class="form-control" placeholder="User Name" required>
                  </div>
                  <div class="form-group">
                    <label>New Password</label>
                    <input type="password" id="newPass" name="newPass" class="form-control" placeholder="New Password" required>
                  </div>
                  <div class="form-group">
                    <label>Confirm New Password</label>
                    <input type="password" id="conPass" name="conPass" class="form-control" placeholder="Confirm New Password" required>
                  </div>   
                  <div class="form-group">
                    <label>Roles</label>
                     <select class="form-control" id="roles" name="roles" multiple>
                     </select>
                  </div>
                </div>
                <div class="modal-footer">
                   <button type="button" id="updateButton" name="update" class="btn btn-info btn-fill btn-block"><span class="glyphicon glyphicon-ok-sign"></span>Update</button>
                </div>
            </div>
          </div>
    </div>
</div>
</sec:authorize>
</body>

    <!--   Core JS Files   -->
    <script src="<%=request.getContextPath()%>/resources/js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-ui.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap.js" type="text/javascript"></script>

    <!--  Table Plugin -->
    <script src="<%=request.getContextPath()%>/resources/js/bootstrap-table.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/resources/js/bootstrap-table-en-US.js" type="text/javascript"></script>
	<!--  Checkbox, Radio & Switch Plugins -->
	<script src="<%=request.getContextPath()%>/resources/js/bootstrap-checkbox-radio-switch.js"></script>

	<!--  Charts Plugin -->
	<script src="<%=request.getContextPath()%>/resources/js/chartist.min.js"></script>

    <!--  Notifications Plugin    -->
    <script src="<%=request.getContextPath()%>/resources/js/bootstrap-notify.js"></script>

    <!-- Light Bootstrap Table Core javascript and methods for Demo purpose -->
	<script src="<%=request.getContextPath()%>/resources/js/light-bootstrap-dashboard.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/handlebars.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/admin.js"></script>
    
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="<%=request.getContextPath()%>/resources/js/ie10-viewport-bug-workaround.js"></script>
</html>
