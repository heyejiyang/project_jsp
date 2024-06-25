<%@ tag body-content="scriptless" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" %>
<fmt:setBundle basename="messages.commons"/>
<c:url var="cssUrl" value="/css/" />
<c:url var="jsUrl" value="/js/" />
<c:url var="homeUrl" value="/"/>
<c:url var="searchUrl" value="/board/search"/>
<c:url var="logoUrl" value="/images/logo.png"/>

<layout:common title="${title}">
    <jsp:attribute name="header">
        <section class="site-top">
            <div class="layout-width inner">
                <div class="left">
                    <a href="${homeUrl}">
                        <i class="xi-home-o"></i>
                        <fmt:message key="홈" />
                    </a>
                </div>
                <div class="right">
                    <a href="<c:url value="/member/join"/>">
                        <i class="xi-user-plus-o"></i>
                        <fmt:message key="회원가입"/>
                    </a>
                    <a href="<c:url value="'/member/login"/> ">
                        <i class="xi-log-in"></i>
                        <fmt:message key="로그인"/>
                    </a>
                </div>
            </div>
        </section>
        <section class="logo-search">
            <div class="layout-width inner">
                <div class="left">
                    <a href="${homeUrl}" class="logo">
                        <img src="${logoUrl}" alt="<fmt:message key="로고"/>">
                    </a>
                </div>
                <div class="right">
                    <form class="search-box" method="GET" action="${searchUrl}" autocomplete="off">
                        <input type="text" name="keyword" placeholder="<fmt:message key="검색어를_입력하세요."/>">
                        <button type="submit">
                            <i class="xi-search"></i>
                        </button>
                    </form>
                </div>
            </div>
        </section>
         <nav>
             <div class="layout-width inner">
                 <a href="#">메뉴1</a>
                 <a href="#">메뉴2</a>
                 <div class="dropdown">
                     <a href="#" id="menu3">메뉴3 <i class="xi-caret-down"></i></a>
                     <div class="dropdown-content">
                         <a href="<c:url value="/member/join"/>">공지사항</a>
                         <a href="<c:url value="/member/join"/>">Q&A</a>
                     </div>
                 </div>
                 <a href="#">메뉴4</a>
                 <a href="#">메뉴5</a>
             </div>
         </nav>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var menu3 = document.getElementById('menu3');
            var dropdownContent = menu3.nextElementSibling; // .dropdown-content

            menu3.addEventListener('click', function(event) {
                event.preventDefault();
                dropdownContent.classList.toggle('show');
            });

            // Close the dropdown menu when clicking outside
            document.addEventListener('click', function(event) {
                if (!menu3.contains(event.target)) {
                    dropdownContent.classList.remove('show');
                }
            });
        });
    </script>
    </jsp:attribute>
    <jsp:attribute name="footer">
        <section class="layout-width inner">
            메엔 레이아웃 하단 영역
        </section>
    </jsp:attribute>
    <jsp:attribute name="commonCss">
        <link rel="stylesheet" type="text/css" href="${cssUrl}main.css">
    </jsp:attribute>
    <jsp:attribute name="commonJs">
        <script src="${jsUrl}main.js"></script>
    </jsp:attribute>
    <jsp:body>
        <jsp:doBody />
    </jsp:body>
</layout:common>