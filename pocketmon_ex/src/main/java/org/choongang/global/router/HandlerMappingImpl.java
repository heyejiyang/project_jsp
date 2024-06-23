package org.choongang.global.router;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.config.annotations.*;
import org.choongang.global.config.containers.BeanContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 웹 애플리케이션의 요청을 처리할 컨트롤러와 메서드를 찾아주는 역할을 함
 */
@Service
public class HandlerMappingImpl implements HandlerMapping{


    private String controllerUrl; //컨트롤러의 URL패턴을 저장하는 변수

    // HTTP 요청을 받아 해당 요청을 처리할 적절한 컨트롤러와 메서드를 찾아 반환하는 역할을 함
    @Override
    public List<Object> search(HttpServletRequest request) {

        List<Object> items = getControllers(); //조건에 맞는 컨트롤러 객체를 가져와서 items리스트에 저장한다.

        for (Object item : items) { //객체를 순회하며 요청을 처리할 수 있는지 확인
            /** Type 애노테이션에서 체크 S */
            // @Target(ElementType.TYPE) - 이 애노테이션이 적용될 수 있는 자바 요소의 타입을 지정, 이 애노테이션이 클래스/인터페이스/열거형에 적용될 수 있음을 의미함
            // @RequestMapping, @GetMapping, @PostMapping, @PatchMapping, @PutMapping, @DeleteMapping
            if (isMatch(request,item.getClass().getDeclaredAnnotations(), false, null)) { //false니까 클래스 레벨 체크
                // isMatch메서드를 호출하여 클래스 레벨 애노테이션이 요청 URL과 매칭되는지 확인한다, false: 메서드가 아닌 클래스 레벨의 애노테이션 체크, null: 컨트롤러 URL 패턴을 아직 설정하지 않았음
                for (Method m : item.getClass().getDeclaredMethods()) { //매칭된 컨트롤러의 모든 메서드 순회
                    if (isMatch(request, m.getDeclaredAnnotations(), true, controllerUrl)) { //true니까 메서드 레벨 체크
                        /*
                        isMatch 메서드를 호출하여 메서드 레벨 애노테이션이 요청 URL과 매칭되는지 확인합니다.
                        true는 메서드 레벨 애노테이션을 체크한다는 의미입니다.
                        controllerUrl은 컨트롤러 클래스 레벨에서 설정된 URL 패턴을 의미합니다.
                         */
                        return List.of(item, m);
                        //매칭된 컨트롤러와 메서드를 리스트로 반환
                        //List.of(item, m)는 item (컨트롤러 객체)와 m (매칭된 메서드)을 리스트로 만듦
                    }
                }
            }
            /** Type 애노테이션에서 체크 E */

            /**
             * Method 애노테이션에서 체크 S
             *  - Type 애노테이션 주소 매핑이 되지 않은 경우, 메서드에서 패턴 체크
             * 애노테이션이 적용될 수 있는 자바 요소의 타입이 TYPE이 아닌 경우 클래스에 정의할 수 없는 경우이기 때문에 클래스에서 패턴 체크는 필요없기 때문
             */
            for (Method m : item.getClass().getDeclaredMethods()) {
                if (isMatch(request, m.getDeclaredAnnotations(), true, null)) {
                    return List.of(item, m);
                }
            }
            /* Method 애노테이션에서 체크 E */
        }

        return null; // 모든 컨트롤러와 메서드를 검사한 후에도 매칭되는 것이 없으면 null을 반환
    }


    /**
     *
     * @param request: 현재 HTTP요청
     * @param annotations : 적용 애노테이션 목록 (클래스나 메서드에 선언된 애노테이션 배열)
     * @param isMethod : 현재 검사 중인 애노테이션이 메서드 애노테이션인지 여부
     * @param prefixUrl : 컨트롤러 타입 애노테이션에서 적용된 경우
     * // 컨트롤러 클래스에 애노테이션으로 지정된 URL 패턴을 메서드에 매핑된 URL 패턴과 결합하여 완전한 URL 경로를 생성하기 위해 사용
     * @return
     */
    /**
     * 주어진 요청이 특정 컨트롤러 메서드와 매핑되는지 확인하는 역할
     * 주어진 요청의 URI와 HTTP 메서드를 기반으로 애노테이션을 검사하여 매핑된 URL 패턴과 일치하는지 확인한다.
     */
    private boolean isMatch(HttpServletRequest request, Annotation[] annotations, boolean isMethod, String prefixUrl) {

        String uri = request.getRequestURI(); //사용자가 요청한 uri
        String method = request.getMethod().toUpperCase(); //요청 HTTP 메서드(GET, POST, PUT 등)를 가져와서 대문자로 변환
        String[] mappings = null; //매핑된 URL 패턴을 저장할 배열을 선언

        for (Annotation anno : annotations) {
            //전달받은 애노테이션 배열을 반복하면서 각각의 애노테이션을 검사함

            if (anno instanceof RequestMapping) { // 모든 요청 방식 매핑
                RequestMapping mapping = (RequestMapping) anno;
                //anno가 RequestMapping 타입임이 확인되었으면, 이를 RequestMapping 타입으로 캐스팅
                mappings = mapping.value();
                //mapping 객체의 value() 메서드를 호출하여, RequestMapping 애노테이션에 설정된 URL 패턴 값을 가져옴
                //예를 들어, @RequestMapping("/path") 애노테이션이 있을 때, value() 메서드는 "/path" 값을 반환
            } else if (anno instanceof GetMapping && method.equals("GET")) { // GET 방식 매핑
                GetMapping mapping = (GetMapping) anno;
                mappings = mapping.value();
            } else if (anno instanceof PostMapping && method.equals("POST")) {
                PostMapping mapping = (PostMapping) anno;
                mappings = mapping.value();
            } else if (anno instanceof PutMapping && method.equals("PUT")) {
                PutMapping mapping = (PutMapping) anno;
                mappings = mapping.value();
            } else if (anno instanceof PatchMapping && method.equals("PATCH")) {
                PatchMapping mapping = (PatchMapping) anno;
                mappings = mapping.value();
            } else if (anno instanceof DeleteMapping && method.equals("DELETE")) {
                DeleteMapping mapping = (DeleteMapping) anno;
                mappings = mapping.value();
            }

            if (mappings != null && mappings.length > 0) { // mappings 배열이 null이 아니고, 적어도 하나의 URL 패턴을 가지고 있는지 확인

                String matchUrl = null;

                if (isMethod) { //만약 isMethod가 true라면(즉, 현재 애노테이션이 메서드 애노테이션이라면)
                    String addUrl = prefixUrl == null ? "" : prefixUrl;
                    //클래스 애노테이션을 검사할 때는 prefixUrl이 null임
                    //메서드 애노테이션을 검사할 때는 prefixUrl이 클래스 레벨에서 매핑된 경로로 설정된다.

                    // 메서드인 경우 *와 {경로변수} 고려하여 처리
                    for(String mapping : mappings) {
                        String pattern = mapping.replace("/*", "/\\w*")
                                .replaceAll("/\\{\\w+\\}", "/(\\\\w*)");
                        /*
                            \w: [a-zA-Z0-9_]

                            mapping.replace("/*", "/\\w*") :  / 뒤에 어떤 단어 문자(숫자, 알파벳 등)가 0개 이상 오는 것을 의미
                            예) /api/* 가 /api/\\w* 로 변환됨

                            .replaceAll("/\\{\\w+\\}", "/(\\\\w*)"): {경로변수} 패턴을 /(\\w*) 로 변환한다.. 이 부분은 / 뒤에 어떤 단어 문자(숫자, 알파벳 등)가 1개 이상 오는 것을 의미
                            예) /api/{id} 가 /api/(\\w*) 로 변환
                        */
                        Pattern p = Pattern.compile("^" + request.getContextPath() + addUrl + pattern + "$");
                        /*
                        ^: 문자열의 시작을 나타냅니다.
                        request.getContextPath(): 애플리케이션의 컨텍스트 경로를 추가합니다.
                        addUrl: 클래스 레벨에서 정의된 기본 경로를 추가합니다.
                        pattern: 메서드 레벨의 매핑 패턴을 추가합니다$: 문자열의 끝을 나타냅니다.
                         */
                        Matcher matcher = p.matcher(uri); //요청된 URI와 정규 표현식을 비교
                        return matcher.find();
                        // URI가 정규 표현식과 일치하는지 확인, 일치하면 true를 반환하고, 그렇지 않으면 false를 반환
                    }
                } else { //현재 애노테이션이 클래스 애노테이션일때
                    List<String> matches = Arrays.stream(mappings)
                            .filter(s -> uri.startsWith(request.getContextPath() + s)).toList();
                    /*Arrays.stream(mappings)을 사용하여 배열을 스트림으로 변환
                    filter를 사용하여 각 URL 패턴이 현재 요청 URI와 매칭되는지 확인

                    s -> uri.startsWith(request.getContextPath() + s)는 각 URL 패턴이 현재 요청 URI로 시작하는지를 검사하는 람다 표현식
                    filter 조건을 만족하는 패턴들을 toList()를 사용하여 리스트로 변환하고, 이 리스트를 matches 변수에 할당
                    */

                    if (!matches.isEmpty()) { //매칭되는 URL패턴이 존재한다면
                        matchUrl = matches.get(0); //matches 리스트에서 첫 번째 매칭된 URL 패턴을 가져와서 matchUrl 변수에 할당
                        controllerUrl = matchUrl;
                    }
                }
                return matchUrl != null && !matchUrl.isBlank(); //null과 빈값이 아닐경우 true반환
            }
        }

        return false; //일치하는 패턴이 없는 경우 false를 반환
    }

    /**
     * 모든 컨트롤러 조회
     *
     * @return
     */
    private List<Object> getControllers() {
        return BeanContainer.getInstance().getBeans().entrySet()
                .stream()
                .map(s -> s.getValue())
                .filter(b -> Arrays.stream(b.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof Controller || a instanceof RestController))
                .toList();
        /*
         1. BeanContainer.getInstance().getBeans().entrySet()
            - BeanContainer의 싱글톤 인스턴스를 가져와서 getBeans메서드를 호출하여 모든 beans객체를 가져온다.
            - beans.entrySet()을 호출하여 Set<Map.Entry<String, Object>>를 얻는다. 이 Set은 맵에 저장된 모든 키-값 쌍(Map.Entry)을 포함한다.

            entrySet: Map에 저장된 모든 키-값쌍을 Set형태로 반환한다.
            Map.Entry는 Map 내부에 정의된 중첩 인터페이스로, 하나의 키-값 쌍을 나타낸다.

          2. .stream(): Set을 스트림으로 변환, 이제 각 요소는 Map.Entry<String, Object> 타입임

          3. .map(s -> s.getValue()): 스트림의 각 Map.Entry 객체에서 값을 추출함, Map.Entry 객체는 getKey와 getValue 메서드를 제공하는데, 여기서는 값(value)을 가져온다. 값은 beans 객체임

          4. .filter(b -> Arrays.stream(b.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof Controller || a instanceof RestController)): 각 beans객체에 클래스에 선언된 애노테이션을 배열로 가져옴 -> 애노테이션 배열을 스트림으로 변환하고 @Controller 또는 @RestController 애노테이션이 붙어 있는지를 확인함, 이 애노테이션이 있는 객체만 필터링

          5. .toList(): 필터링된 스트림을 리스트로 변환하여  해당 객체를 반환한다.
         */
    }
}