package org.choongang.global.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.global.config.annotations.*;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HandlerAdapterImpl implements HandlerAdapter {

    private final ObjectMapper om;

    public HandlerAdapterImpl() {
        om = new ObjectMapper(); //Java 객체를 JSON으로 변환하거나 JSON을 Java 객체로 변환하는 데 사용
        om.registerModule(new JavaTimeModule()); //날짜 및 시간처리를 위함
        //registerModule(new JavaTimeModule()) 메서드는 ObjectMapper에게 Java 8의 날짜와 시간 처리를 위한 기능을 추가하도록 지시
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, List<Object> data) {

        Object controller = data.get(0); // 컨트롤러 객체
        //data 리스트에서 첫번째 요소를 컨트롤러 객체로 설정
        Method method = (Method)data.get(1); // 찾은 요청 메서드
        //data 리스트에 두번째 요소를 요청된 메서드로 설정

        String m = request.getMethod().toUpperCase(); // 요청 메서드
        //HTTP 요청 메서드를 대문자로 변환한다.(GET, POST...)
        Annotation[] annotations = method.getDeclaredAnnotations();
        //메서드에 있는 모든 annotation 배열에 저장

        /* 컨트롤러 애노테이션 처리 S */
        String[] rootUrls = {""}; //루트 URL배열을 초기화
        for (Annotation anno : controller.getClass().getDeclaredAnnotations()) {  //컨트롤러 클래스의 선언된 모든 애노테이션을 반복하여 처리함
            rootUrls = getMappingUrl(m, anno); //클래스 레벨의 애노테이션들을 확인하고, 그 중에서도 HTTP 요청 메서드에 맞는 URL 패턴을 추출하여 rootUrls에 저장
            //RequestMapping이 없다면 값이 없는경우임
        }
        /* 컨트롤러 애노테이션 처리 E */

        //메서드 애노테이션 처리
        //경로 변수는 URL의 일부를 동적으로 처리할 수 있도록 하는 기능으로, 예를 들어 /users/{userId}와 같이 요청 URL의 일부를 매개변수처럼 사용할 수 있습니다.

        /* PathVariable : 경로 변수 패턴 값 추출
         -> URL패턴에서 {}로 둘러싸인 부분을 의미함, 즉, 경로변수를 추출하는 것을 의미
         S */
        String[] pathUrls = {""}; //경로 변수 URL 배열을 초기화
        Map<String, String> pathVariables = new HashMap<>();
        //경로 변수와 그 값들을 담을 맵을 초기화
        for (Annotation anno : annotations) {//요청된 메서드의 모든 애노테이션을 반복하여 처리
            pathUrls = getMappingUrl(m, anno); //해당 애노테이션이 매핑하는 URL패턴을 가져옴
        }

        //경로변수 매칭 처리
        if (pathUrls != null) { //메서드에 매핑된 URL패턴이 존재하는 경우에만 처리
            Pattern p = Pattern.compile("\\{(\\w+)\\}");
            /*
            \{: 역슬래시 \는 특수 문자를 이스케이프(escape)하는 역할을 합니다. 여기서 {는 문자 그대로의 중괄호를 표현합니다.
            (\\w+): 괄호 안의 \w+는 정규 표현식 메타 문자로, 문자와 숫자를 포함한 모든 단어 문자를 나타냅니다. +는 앞의 문자나 그룹이 하나 이상 나타남을 의미합니다.
            \}: 다시 역슬래시를 사용해 중괄호를 이스케이프하여 문자 그대로의 중괄호 }를 나타냅니다.
             */
            for (String url : pathUrls) { //메서드에 정의된 모든 URL 패턴 반복
                Matcher matcher = p.matcher(url);
                // p를 사용하여 주어진 문자열 url에서 패턴을 찾기 위한 Matcher 객체를 생성하는 코드
                //패턴(p)과 문자열에(url) 일치하는 패턴이 있으면 matcher에 저장

                List<String> matched = new ArrayList<>();
                while (matcher.find()) { //matcher에 값이 있으면 true
                    matched.add(matcher.group(1));
                    //일치된 패턴들 저장한 matcher에서 group -> 일치한 문자열들 가져옴/
                    // 추출된 경로 변수 이름들을 matched 리스트에 추가
                    //group(0)은 전체 매칭된 문자열을 반환하고, group(1)은 첫 번째 캡처 그룹에 매칭된 문자열을 반환
                }

                if (matched.isEmpty()) continue;  // 만약 경로 변수가 없으면 처리를 건너뜁니다.

                for (String rUrl : rootUrls) {
                    String _url = request.getContextPath() + rUrl + url; //전체 URL 생성
                    //루트 URL과 경로 변수를 합쳐 전체 URL을 생성합니다.

                    for (String s : matched) {
                        _url = _url.replace("{" + s + "}", "(\\w*)");//"{" + s + "}"를 "(\\w*)") 대체
                    } //경로 변수 자리에 정규 표현식을 사용하여 매칭할 수 있도록

                    Pattern p2 = Pattern.compile("^" + _url+"$"); //URL을 정규 표현식으로 변환
                    Matcher matcher2 = p2.matcher(request.getRequestURI());
                    //p2 패턴과 request.getRequestURI() 일치하는 패턴이 있으면 대입
                    while (matcher2.find()) {
                        for (int i = 0; i < matched.size(); i++) {
                            pathVariables.put(matched.get(i), matcher2.group(i + 1));
                            //"(\\w*)" 이게 그룹화로 경로가  @GetMapping("/{mode}/test/{num}") 이라고 할때 {mode} 가 group1로 {num}이 그룹 2로 잡힌다.
                        } //매칭된 경로 변수와 값을 pathVariables 맵에 저장
                    }
                }
            }
        }

        /* PathVariable : 경로 변수 패턴 값 추출 E */

        /* 메서드 매개변수 의존성 주입 처리 S */
        List<Object> args = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            try {
                Class cls = param.getType();
                String paramValue = null;
                for (Annotation pa : param.getDeclaredAnnotations()) {
                    if (pa instanceof RequestParam requestParam) { // 요청 데이터 매칭
                        String paramName = requestParam.value();
                        paramValue = request.getParameter(paramName);
                        break;
                    } else if (pa instanceof PathVariable pathVariable) { // 경로 변수 매칭
                        String pathName = pathVariable.value();
                        paramValue = pathVariables.get(pathName);
                        break;
                    }
                }

                if (cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == double.class || cls == Double.class ||  cls == float.class || cls == Float.class) {
                    paramValue = paramValue == null || paramValue.isBlank()?"0":paramValue;
                }

                if (cls == HttpServletRequest.class) {
                    args.add(request);
                } else if (cls == HttpServletResponse.class) {
                    args.add(response);
                } else if (cls == int.class) {
                    args.add(Integer.parseInt(paramValue));
                } else if (cls == Integer.class) {
                    args.add(Integer.valueOf(paramValue));
                } else if (cls == long.class) {
                    args.add(Long.parseLong(paramValue));
                } else if (cls == Long.class) {
                    args.add(Long.valueOf(paramValue));
                } else if (cls == float.class) {
                    args.add(Float.parseFloat(paramValue));
                } else if (cls == Float.class) {
                    args.add(Float.valueOf(paramValue));
                } else if (cls == double.class) {
                    args.add(Double.parseDouble(paramValue));
                } else if (cls == Double.class) {
                    args.add(Double.valueOf(paramValue));
                } else if (cls == String.class) {
                    // 문자열인 경우
                    args.add(paramValue);
                } else {
                    // 기타는 setter를 체크해 보고 요청 데이터를 주입
                    // 동적 객체 생성
                    Object paramObj = cls.getDeclaredConstructors()[0].newInstance();
                    for (Method _method : cls.getDeclaredMethods()) {
                        String name = _method.getName();
                        if (!name.startsWith("set")) continue;

                        char[] chars = name.replace("set", "").toCharArray();
                        chars[0] = Character.toLowerCase(chars[0]);
                        name = String.valueOf(chars);
                        String value = request.getParameter(name);
                        if (value == null) continue;


                        Class clz = _method.getParameterTypes()[0];
                        // 자료형 변환 후 메서드 호출 처리
                        invokeMethod(paramObj,_method, value, clz, name); //메서드 동적 호출
                    }
                    args.add(paramObj);
                } // endif
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        /* 메서드 매개변수 의존성 주입 처리 E */

        /* 요청 메서드 호출 S */
        try {
            Object result = args.isEmpty() ? method.invoke(controller) : method.invoke(controller, args.toArray());

            /**
             *  컨트롤러 타입이 @Controller이면 템플릿 출력,
             * @RestController이면 JSON 문자열로 출력, 응답 헤더를 application/json; charset=UTF-8로 고정
             */
            boolean isRest = Arrays.stream(controller.getClass().getDeclaredAnnotations()).anyMatch(a -> a instanceof RestController);
            // Rest 컨트롤러인 경우
            if (isRest) {
                response.setContentType("application/json; charset=UTF-8");
                String json = om.writeValueAsString(result);
                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }

            // 일반 컨트롤러인 경우 문자열 반환값을 템플릿 경로로 사용
            String tpl = "/WEB-INF/templates/" + result + ".jsp";
            RequestDispatcher rd = request.getRequestDispatcher(tpl);
            rd.forward(request, response);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage()); //처리되지 않은 예외 500.jsp 유입
        }
        /* 요청 메서드 호출 E */
    }

    /**
     * 자료형 변환 후 메서드 호출 처리
     *
     * @param paramObj
     * @param method
     * @param value
     * @param clz
     * @param fieldNm - 멤버변수명
     */
    private void invokeMethod(Object paramObj, Method method, String value, Class clz, String fieldNm) {
        try {
            if (clz == String.class) { // 문자열 처리
                method.invoke(paramObj, value);

                /* 기본 자료형 및 Wrapper 클래스 자료형 처리  S */
            } else if (clz == boolean.class) {
                method.invoke(paramObj, Boolean.parseBoolean(value));
            } else if (clz == Boolean.class) {
                method.invoke(paramObj, Boolean.valueOf(value));
            } else if (clz == byte.class) {
                method.invoke(paramObj, Byte.parseByte(value));
            } else if (clz == Byte.class) {
                method.invoke(paramObj, Byte.valueOf(value));
            } else if (clz == short.class) {
                method.invoke(paramObj, Short.parseShort(value));
            } else if (clz == Short.class) {
                method.invoke(paramObj, Short.valueOf(value));
            } else if (clz == int.class) {
                method.invoke(paramObj, Integer.parseInt(value));
            } else if (clz == Integer.class) {
                method.invoke(paramObj, Integer.valueOf(value));
            } else if (clz == long.class) {
                method.invoke(paramObj, Long.parseLong(value));
            } else if (clz == Long.class) {
                method.invoke(paramObj, Long.valueOf(value));
            } else if (clz == float.class) {
                method.invoke(paramObj, Float.parseFloat(value));
            } else if (clz == Float.class) {
                method.invoke(paramObj, Float.valueOf(value));
            } else if (clz == double.class) {
                method.invoke(paramObj, Double.parseDouble(value));
            } else if (clz == Double.class) {
                method.invoke(paramObj, Double.valueOf(value));
                /* 기본 자료형 및 Wrapper 클래스 자료형 처리 E */
                // LocalDate, LocalTime, LocalDateTime 자료형 처리 S
            } else if (clz == LocalDateTime.class || clz == LocalDate.class || clz == LocalTime.class) {
                Field field = paramObj.getClass().getDeclaredField(fieldNm);
                for (Annotation a : field.getDeclaredAnnotations()) {
                    if (a instanceof DateTimeFormat dateTimeFormat) {
                        String pattern = dateTimeFormat.value();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                        if (clz == LocalTime.class) {
                            method.invoke(paramObj, LocalTime.parse(value, formatter));
                        } else if (clz == LocalDate.class) {
                            method.invoke(paramObj, LocalDate.parse(value, formatter));
                        } else {
                            method.invoke(paramObj, LocalDateTime.parse(value, formatter));
                        }
                        break;
                    } // endif
                } // endfor
                // LocalDate, LocalTime, LocalDateTime 자료형 처리 E
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 요청 메서드 & 애노테이션으로 설정된 mapping Url 조회
     *
     * @param method - HTTP요청메서드
     * @param anno
     * @return
     */
    private String[] getMappingUrl(String method, Annotation anno) {

        // RequestMapping은 모든 요청에 해당하므로 정의되어 있다면 이 설정으로 교체하고 반환한다.
        if (anno instanceof  RequestMapping) {
            //이 애노테이션은 모든 HTTP 요청 메서드에 대한 URL 매핑을 지원
            RequestMapping mapping = (RequestMapping) anno;
            return mapping.value(); //해당 URL 패턴 배열을 가져온다.
        }

        if (method.equals("GET") && anno instanceof GetMapping) {
            GetMapping mapping = (GetMapping) anno;
            return mapping.value();
            //해당하는 HTTP 메서드와 일치할 때 그에 맞는 mapping.value()를 반환
        } else if (method.equals("POST") && anno instanceof PostMapping) {
            PostMapping mapping = (PostMapping) anno;
            return mapping.value();
        } else if (method.equals("PATCH") && anno instanceof PatchMapping) {
            PatchMapping mapping = (PatchMapping) anno;
            return mapping.value();
        } else if (method.equals("PUT") && anno instanceof PutMapping) {
            PutMapping mapping = (PutMapping) anno;
            return mapping.value();
        } else if (method.equals("DELETE") && anno instanceof DeleteMapping) {
            DeleteMapping mapping = (DeleteMapping) anno;
            return mapping.value();
        }

        return null; //메서드가 특정 HTTP 메서드와 일치하는 애노테이션을 찾지 못하면 null을 반환
    }
}