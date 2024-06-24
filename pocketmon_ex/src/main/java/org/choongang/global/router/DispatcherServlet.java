package org.choongang.global.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.containers.BeanContainer;

import java.io.IOException;

/**
 * 모든 HTTP 요청을 받아서 HttpServletRequest와 HttpServletResponse 객체로 변환한 후, IoC 컨테이너(BeanContainer)에 추가합니다. 그런 다음 BeanContainer를 통해 RouterService 빈을 가져와서, 실제 라우팅을 수행하는 route 메서드를 호출합니다. 이는 각 요청을 적절한 컨트롤러와 메서드로 라우팅하여 처리하는 역할
 */
@WebServlet("/")
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        BeanContainer bc = BeanContainer.getInstance();
        bc.addBean(HttpServletRequest.class.getName(), request);
        bc.addBean(HttpServletResponse.class.getName(), response);

        bc.loadBeans();

        RouterService service = bc.getBean(RouterService.class);
        service.route(request, response);
    }
}