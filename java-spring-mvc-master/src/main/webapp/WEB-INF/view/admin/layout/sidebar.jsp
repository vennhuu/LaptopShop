<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <style>
      /* Hover màu đỏ cho nav-link */
      .sb-sidenav .nav-link:hover {
        background-color: #e53935 !important;
        /* đỏ tươi */
        color: white !important;
      }

      .sb-sidenav .nav-link:hover .sb-nav-link-icon i {
        color: white !important;
      }
    </style>
    <div id="layoutSidenav_nav">
      <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
          <div class="nav">
            <div class="sb-sidenav-menu-heading">Features</div>

            <a class="nav-link" href="/admin">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              Dashboard
            </a>

            <a class="nav-link" href="/admin/user">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              User
            </a>

            <a class="nav-link" href="/admin/product">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              Product
            </a>

            <a class="nav-link" href="/admin/order">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              Order
            </a>

            <div class="sb-sidenav-menu-heading">Khác</div>

            <a class="nav-link" href="/admin/cmtCus">
              <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
              Feedback
            </a>
          </div>
        </div>
        <div class="sb-sidenav-footer">
          <div class="small">Logged in as:</div>
          <c:out value="${pageContext.request.userPrincipal.name}" />
        </div>
      </nav>
    </div>