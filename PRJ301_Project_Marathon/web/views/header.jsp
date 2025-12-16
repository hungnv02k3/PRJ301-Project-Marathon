<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="/PRJ301_Project_Marathon/static/css/styles.css"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header id="header">
    <div class="header_top">
        <div class="container">
            <div class="row">
                <div class="col-sm-6">
                    <div class="contactinfo">
                        <ul class="nav nav-pills">
                            <li><a href="#"><i class="fa fa-phone"></i> +2 95 01 88 821</a></li>
                            <li><a href="#"><i class="fa fa-envelope"></i> info@domain.com</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="social-icons pull-right">
                        <ul class="nav navbar-nav">
                            <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                            <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                            <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                            <li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="header-middle">
        <div class="container">
            <div class="row">
                <div class="col-sm-4">
                    <div class="logo pull-left">
                        <a href="${pageContext.request.contextPath}/home">
                            Home
                        </a>
                    </div>
                </div>
                <div class="col-sm-8">
                    <div class="shop-menu pull-right">
                        <ul class="nav navbar-nav">
                            <c:choose>
                                <c:when test="${not empty sessionScope.account}">
                                    <li>
                                        <a href="#"><i class="fa fa-user"></i>
                                            ${sessionScope.account.getUserName()}
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#" onclick="openLogoutModal()">
                                            <i class="fa fa-sign-out"></i> Logout
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                                    <li><a href="${pageContext.request.contextPath}/signup">Signup</a></li>
                                    </c:otherwise>
                                </c:choose>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="logoutModal" class="modal-overlay">
        <div class="modal-box">
            <h3>Confirm Logout</h3>
            <p>Are you sure you want to logout?</p>

            <div class="modal-actions">
                <button class="btn-cancel" onclick="closeLogoutModal()">Cancel</button>
                <button class="btn-logout" onclick="doLogout()">Logout</button>
            </div>
        </div>
    </div>
</header>
<!-- Logout Modal -->

<script type="text/javascript">
    function openLogoutModal() {
        var modal = document.getElementById("logoutModal");
        if (modal) {
            modal.style.display = "flex";
        }
    }

    function closeLogoutModal() {
        var modal = document.getElementById("logoutModal");
        if (modal) {
            modal.style.display = "none";
        }
    }

    function doLogout() {
        window.location.href = "${pageContext.request.contextPath}/logout";
    }

    window.onclick = function (event) {
        var modal = document.getElementById("logoutModal");
        if (event.target === modal) {
            closeLogoutModal();
        }
    };
</script>
