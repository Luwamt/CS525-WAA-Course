package lab_1;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyInjector {

    private  static Map<Class, Object> container = new HashMap<>();

    public  static void scanBean() throws IllegalAccessException, InstantiationException {
// Search
        Reflections reflections =
                new Reflections("lab_1", new SubTypesScanner(),
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner());
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(MYBean.class);
        /**
         *   put the to map
         */

        for (Class<?> claz : annotatedClasses) {
            Object obj = claz.newInstance();
            container.put(claz, obj);
        }

    }

    public static void   scanForAutowired() throws IllegalAccessException {
        //search autowire
        Reflections reflections =
                new Reflections("lab_1", new SubTypesScanner(),
                        new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner());
        Set<Field> annotatedField = reflections.getFieldsAnnotatedWith(MyAutowired.class);
        for (Field field : annotatedField) {

            Object className = field.getDeclaringClass();
            Object fieldType = field.getType();

            Object classObject = container.get(className);
            Object fieldObject = container.get(fieldType);

            field.setAccessible(true);
            field.set(classObject, fieldObject);
        }
        for(Map.Entry<Class,Object> map: container.entrySet()){
            System.out.println(map.getValue());
        }
    }
    public static Object  getBean(Classc clazz) throws BeanNotFoundException {
if(container.containsKey(clazz)){
    return container.get(clazz);
}else
    throw  new BeanNotFoundException("Class not found ");

    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, BeanNotFoundException {
        scanBean();
        scanForAutowired();
        Classc c = new Classc();
        getBean(c);
    }
}