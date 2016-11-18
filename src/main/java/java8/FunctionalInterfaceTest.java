package java8;

import org.junit.Test;

/**
 * Created by yingmei.qym on 2016/11/17.
 */
public class FunctionalInterfaceTest {
    @Test
    public void test(){
        Converter<String, Integer> a = (from) -> Integer.valueOf(from);
        Converter2<String, Integer> b = (from) -> Integer.valueOf(from);
        //Java 8 允许你使用 :: 关键字来传递方法或者构造函数引用
        b =  Integer::valueOf; //这个是静态方式导入
    }

    interface Converter<F, T> {
        T convert(F f);
    }
    //下面这个是编译不过的，因为函数式接口只能有一个抽象方法
//    @FunctionalInterface
//    interface Converter2<F, T> {
//        T convert1(F t);
//        F convert2(T t);
//    }

    //然而下面这个是能编译过的，因为这个函数式接口只有一个抽象方法convert1
    @FunctionalInterface
    interface Converter2<F, T> {
        T convert1(F t);
        default Integer convert2(String t){ return null;}
    }

    @Test
    public void test2(){
        //测试lambda作用域
        String a = "aa";
        Converter<String, Integer> converter = (String s) ->Integer.valueOf(a + s);
        converter = (s) ->Integer.valueOf(a + s);
        Integer t = 1;
        converter = (s)->Integer.valueOf(t + s);
    }
}
