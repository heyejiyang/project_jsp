package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


/*
DispatcherServlet의 service 메서드가 모든 HTTP 요청을 처리하기 때문에, CSS, JS 파일과 같은 정적 자원에 대한 요청도 DispatcherServlet 서블릿을 통과하게 된다는 것입니다. 이로 인해 정적 자원이 정상적으로 제공되지 않을 수 있습니다. 따라서 이러한 정적 자원 요청을 별도로 처리해 줄 필요가 있습니다.
그러나 DispatcherServlet은 이러한 정적 자원(CSS, JS, 이미지 등)을 직접 처리하는 기능이 없기 때문에, 정적 자원이 제대로 제공되지 않습니다.
 */
/*
 * 정적 자원에 대한 요청을 DispatcherServlet이 처리하지 않도록 설정해야 합니다.
 * 이를 위해 웹 서버나 서블릿 컨테이너 설정에서 정적 자원 경로를 분리하고, 해당 경로의 요청은 DispatcherServlet을 거치지 않고 직접 웹 서버에서 처리하도록 합니다.
 */

/**
 * 정적 자원을 처리하는 방법 구현
 * 정적 자원 경로를 확인하고 해당 자원을 클라이언트에게 제공하는 기능을 한다.
 */
@Service //서비스 클래스임을 나타냄
public class StaticResourceMappingImpl implements StaticResourceMapping {

    /**
     * 정적 자원 경로인지 체크
     *
     * @param request
     * @return
     */
    @Override
    public boolean check(HttpServletRequest request) { //HttpServletRequest 객체를 받아 정적 자원 경로에 해당 파일이 존재하는지 확인

        // webapp/static 경로 유무 체크
        return getStaticPath(request).exists(); //getStaticPath 메서드를 사용하여 파일 경로를 확인하고, 파일이 존재하면 true, 존재하지 않으면 false를 반환

    }

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // webapp/static 경로 처리 S
        File file = getStaticPath(request);
        //getStaticPath 메서드를 사용하여 파일 경로를 확인하고, 파일이 존재하면 파일 내용을 응답으로 출력
        if (file.exists()) { //file객체가 실제로 파일 시스템에 존재하는지 확인하고 존재하지 않으면 이후 코드는 실행되지않는다.
            Path source = file.toPath();
            //file객체를 path객체로 변환

            String contentType = Files.probeContentType(source); // 파일의 MIME 타입을 결정(파일의 형식) 예를 들어 "text/html", "image/png", "application/javascript" 등이 있습니다.

            response.setContentType(contentType); //MIME 타입을 HttpServletResponse 객체의 contentType으로 설정하여 클라이언트가 파일 형식을 알 수 있도록 합니다.

            OutputStream out = response.getOutputStream();
            //클라이언트로 데이터를 보내기 위해 HttpServletResponse 객체의 출력 스트림을 엽니다.
            //파일 데이터를 클라이언트로 전송

            InputStream in = new BufferedInputStream(new FileInputStream(file));
            //파일을 읽기 위해 InputStream을 엽니다.
            //BufferedInputStream은 파일 읽기 성능을 향상시키기 위해 버퍼링을 추가합니다.
            //FileInputStream은 파일 시스템에서 실제 파일을 읽는 스트림입니다.
            out.write(in.readAllBytes()); //byte 배열을 응답 출력 스트림으로 전송
            return;
            //클라이언트에게 파일을 전송한 후 메서드 종료
        }
        // webapp/static 경로 처리 E
    }

    //HttpServletRequest 객체를 받아 실제 파일 시스템에서의 정적 자원 파일 경로를 반환
    private File getStaticPath(HttpServletRequest request) { //HttpServletRequest 객체를 받아 실제 파일 경로를 반환
        String uri = request.getRequestURI().replace(request.getContextPath(), ""); //요청 URI에서 컨텍스트 경로를 제거
        /*
        request.getRequestURI()는 클라이언트의 전체 요청 URI를 반환합니다.
예: /myapp/static/css/style.css
request.getContextPath()는 웹 애플리케이션의 컨텍스트 경로를 반환합니다.
예: /myapp
replace(request.getContextPath(), "")는 전체 요청 URI에서 컨텍스트 경로를 제거하여 상대 경로를 얻습니다.
결과: /static/css/style.css
         */
        String path = request.getServletContext().getRealPath("/static");
        //webapp/static 디렉토리를 기준으로 파일 경로를 생성
        /*
        request.getServletContext().getRealPath("/static")는 웹 애플리케이션의 webapp/static 디렉토리의 실제 파일 시스템 경로를 반환합니다.
예: /path/to/your/webapp/static
         */
        File file = new File(path + uri);
        /*
        path와 uri를 합쳐 최종 파일 경로를 생성합니다.
예: /path/to/your/webapp/static/css/style.css
new File(path + uri)는 이 경로를 나타내는 File 객체를 생성
         */

        return file; //최종적으로 생성된 File객체를 반환
        // 요청된 정적 자원이 실제로 저장된 파일 시스템 경로임
    }
}