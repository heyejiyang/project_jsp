package org.choongang.global.config.containers;

import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.Service;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 특정 애노테이션이 붙은 클래스를 찾아서 싱글톤 객체로 생성하고 관리한다.
 */
public class BeanContainer {
    private static BeanContainer instance; //빈컨테이너의 싱글톤 인스턴스

    private Map<String, Object> beans; //생성된 객체 담는 변수

    public BeanContainer() { //빈 컨테이너 기본 생성자, 객체 생성
        beans = new HashMap<>();
    }

    /**
     * BeanContainer가 클래스 경로를 스캔하여 특정 애노테이션이 붙은 클래스를 찾아 객체를 생성하고 의존성을 해결하는 핵심 메서드
     */
    public void loadBeans() {
        // 패키지 경로 기준으로 스캔 파일 경로 조회
        try {
            String rootPath = new File(getClass().getResource("../../../").getPath()).getCanonicalPath(); //현재 클래스의 경로를 기준으로 루트 경로를 조회한다.
            //이 경로는 BeanContainer 클래스 파일이 위치한 디렉토리로부터 3단계 위로 올라간다.
/* getPath()의 결과
첫 번째 ..: D:\project_jsp\pocketmon_ex\src\main\java\org\choongang\global\config
두 번째 ..: D:\project_jsp\pocketmon_ex\src\main\java\org\choongang\global
세 번째 ..: D:\project_jsp\pocketmon_ex\src\main\java\org\choongang

세번째 경로에 대한 File 객체를 생성한다.
getCanonicalPath()는 이 파일의 정규화된 경로(즉, 경로에 있는 모든 ..과 .을 해석한 결과)를 반환
 */
            String packageName = getClass().getPackageName().replace(".global.config.containers", "");
            //getClass().getPackageName(): 현재 클래스의 패키지 명을 가져오고 '.global.config.containers' 부분을 제거하여 베이스 패키지명을 얻습니다. -> org.choongang / 클래스 경로를 설정하거나 패키지 스캔의 시작점으로 사용

            List<Class> classNames = getClassNames(rootPath, packageName);
            //루트 경로와 패키지명을 기반으로 클래스 이름 목록을 가져온다.

            for (Class clazz : classNames) { //찾은 모든 클래스에 대해 반복
                // 클래스가 인터페이스인 경우 객체 생성을 하지 않으므로 건너뛴다.
                if (clazz.isInterface()) {
                    continue;
                }

                // 애노테이션 중 Controller, RestController, Component, Service 등이 TYPE 애노테이션으로 정의된 경우 beans 컨테이너에 객체 생성하여 보관
                // 키값은 전체 클래스명, 값은 생성된 객체
                String key = clazz.getName();

                // 이미 생성된 객체라면 생성된 객체로 활용, 이미 생성된 객체인경우 건너뜀
                if (beans.containsKey(key)) continue;
                //containsKey는 true/false로 반환
                //Map의 키값은 클래스명, Map에 이미 등록되어있는 key일경우 continue


                Annotation[] annotations = clazz.getDeclaredAnnotations(); //클래스에 정의된 모든 애노테이션을 가져온다.
                //clazz.getDeclaredAnnotations()는 클래스(clazz)에 직접 선언된 모든 애노테이션을 배열 형태로 반환한다.
                //애노테이션 클래스 자체에 붙어있는 메타애노테이션은 반환되지 않는다.

                //클래스가 Bean으로 등록될 수 있는지 확인
                boolean isBean = false;
                /*
                 @Controller, @RestController, @Service, @Component 중 하나라도 존재하는지 확인
                 */
                for (Annotation anno : annotations) {
                    if (anno instanceof Controller || anno instanceof RestController || anno instanceof Service || anno instanceof Component)  {
                        isBean = true; // 확인된 애노테이션이 있으면 isBean을 true로 설정
                        break;
                    }
                }
                // 컨테이너가 관리할 객체라면 생성자 매개변수의 의존성을 체크하고 의존성이 있다면 해당 객체를 생성하고 의존성을 해결한다.
                if (isBean) {
                    Constructor con = clazz.getDeclaredConstructors()[0]; //클래스의 첫번째 생성자를 가져옴
                    List<Object> objs = resolveDependencies(key, con);
                    //생성자의 의존성을 해결하고 필요한 객체들을 가져온다.
                    if (!beans.containsKey(key)) { //아직 bean맵에 객체가 없을 경우
                        Object obj = con.getParameterTypes().length == 0 ? con.newInstance() : con.newInstance(objs.toArray());
                        // 생성자의 파라미터가 없는 경우와 있는 경우로 나누어 객체를 생성합니다.
                        //기본생성자일경우 con.newInstance 아닐경우 뒤에꺼
                        beans.put(key, obj); //생성된 객체를 map에 추가
                    }
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BeanContainer getInstance() { //빈 컨테이너 객체 생성(싱글톤)
        if (instance == null) {
            instance = new BeanContainer();
        }

        return instance;
    }

    /**
     * 생성된 객체 조회
     *
     * @param clazz
     * @return
     */
    public <T> T getBean(Class clazz) {
        return (T)beans.get(clazz.getName());
    }

    public void addBean(Object obj) {

        beans.put(obj.getClass().getName(), obj);
    }

    public void addBean(String key, Object obj) {
        beans.put(key, obj);
    }

    // 전체 컨테이너 객체 반환
    public Map<String, Object> getBeans() {
        return beans;
    }

    /**
     * 주어진 클래스(key에 해당하는 클래스)의 생성자(con)에 필요한 의존성 객체들을 재귀적으로 생성하고 반환하는 역할을 한다.
     *
     * @param con
     */
    private List<Object> resolveDependencies(String key, Constructor con) throws Exception {
        List<Object> dependencies = new ArrayList<>(); //생성한 의존성 객체들을 담는 리스트
        if (beans.containsKey(key)) { //Map에 저장되어있는 클래스(객체가 생성되어있는 클래스인 경우)
            dependencies.add(beans.get(key)); //해당 객체 리스트에 추가
            return dependencies; //반환
        }

        Class[] parameters = con.getParameterTypes(); //생상자 con의 매개변수 타입들을 배열에 저장(ex String, int, 객체 자료형이 될수도?...)
        if (parameters.length == 0) { //매개변수가 없는 기본생성자의 경우
            Object obj = con.newInstance(); //기본생성자를 이용해서 객체 생성
            dependencies.add(obj); //해당 객체리스트에 추가
        } else { //매개변수가 있는 생성자의 경우
            for(Class clazz : parameters) { //parameters에 저장된 타입들 하나씩 clazz에 반환

                Object obj = beans.get(clazz.getName());
                //beans 맵에서 clazz.getName()을 키로 사용하여 해당 타입의 객체(obj)를 가져옴
                if (obj == null) { //해당 타입의 객체가 맵에 등록되어있지 않은 상태
                    Constructor _con = clazz.getDeclaredConstructors()[0];
                    //해당 타입의 첫번째 선언된 생성자를 가져옴

                    if (_con.getParameterTypes().length == 0) { //매개변수가 없는 경우
                        obj = _con.newInstance(); //기본 생성자로 객체 생성
                    } else {
                        List<Object> deps = resolveDependencies(clazz.getName(), _con); //재귀적으로 의존성 해결
                        obj = _con.newInstance(deps.toArray());
                        //deps 리스트에 저장된 의존성 객체들을 배열 형태로 전달하여 객체(obj)를 생성
                    }
                }
                dependencies.add(obj); //생성된 객체를 리스트에 추가
            }
        }


        return dependencies; //의존성 객체 리스트반환
    }

    //이 메서드의 목적: 파일 시스템의 절대 경로를 Java 패키지 경로로 변환하여 클래스 이름을 생성하는 것
    private List<Class> getClassNames(String rootPath, String packageName) {
        List<Class> classes = new ArrayList<>();
        List<File> files = getFiles(rootPath); //rootPath아래 모든 파일들을 리스트로 반환한다.
        for (File file : files) {
            String path = file.getAbsolutePath(); //파일의 절대경로를 가져옵니다.
            // ex) D:\project_jsp\pocketmon_ex\src\main\java\org\choongang\global\config\containers\Example.class
            String className = packageName + "." + path.replace(rootPath + File.separator, "").replace(".class", "").replace(File.separator, ".");
            //패키지 이름과 파일 경로를 조합하여 클래스 이름 생성
            //packageName -> org.choongang
            //path.replace(rootPath + File.separator, "") -> 루트확장자 경로까지 제거
            //path 에서 .class확장자 제거
            //.replace(File.separator, ".") 파일구분자를 .으로 변환

            try {
                Class cls = Class.forName(className); //클래스 이름을 통해 클래스 객체를 생성
                classes.add(cls); //생성된 클래스 객체를 리스트에 추가
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); //객체생성 실패시 예외처리
            }
        }
        return classes; //클래스 객체 리스트 반환
    }

    private List<File> getFiles(String rootPath) {
        List<File> items = new ArrayList<>();
        File[] files = new File(rootPath).listFiles();
        //rootPath를 File 객체로 만들어 해당 경로의 모든 파일과 디렉토리를 배열로 가져옴
        //listFiles() 메서드는 해당 디렉토리의 파일과 서브 디렉토리를 File 객체 배열로 반환한다.

        if (files == null) return items; //files가 null인 경우(즉, rootPath가 유효한 디렉토리가 아닌 경우) 빈 리스트를 반환

        for (File file : files) {
            if (file.isDirectory()) { //현재 파일이 디렉토리인지 확인
                List<File> _files = getFiles(file.getAbsolutePath());
                //file이 디렉토리인 경우, 해당 디렉토리의 절대 경로를 getFiles 메서드로 재귀 호출하여 그 디렉토리 안의 파일 목록을 가져온다.

                if (!_files.isEmpty()) items.addAll(_files);
                //재귀 호출의 결과로 반환된 파일 리스트(_files)가 비어있지 않다면, items 리스트에 모두 추가한다
            } else {
                items.add(file); //file이 디렉토리가 아닌 일반 파일인 경우, items 리스트에 추가
            }
        }
        return items;
    }
}