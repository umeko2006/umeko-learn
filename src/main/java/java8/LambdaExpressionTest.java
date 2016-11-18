package java8;

import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * Created by yingmei.qym on 2016/11/16.
 */

public class LambdaExpressionTest {
    @Test
    public void beforeLambda(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("before using lambda");
            }
        };
        runnable.run();
    }

    @Test
    public void useLambdaRunnable(){
        Runnable runnable = ()-> System.out.println("using lambda");
        runnable.run();
        List<String> arrayList = Arrays.asList("a", "b");

    }

    @Test
    public void useLambdaSort(){
        List<String> names = Arrays.asList("xiaoming", "wanggang", "lilei");
        Collections.sort(names);
        System.out.println(names);
        //如果想要逆序排列呢?
        //只需要给静态方法 Collections.sort 传入一个List对象以及一个比较器来按指定顺序排列。
        // 通常做法都是创建一个匿名的比较器对象然后将其传递给sort方法。
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(names);
        //使用lambda表达式以后,就没必要使用这种传统的匿名对象的方式了，Java 8提供了更简洁的语法
        names = Arrays.asList("xiaoming", "wanggang", "lilei");
        Collections.sort(names, (String a, String b)->{ return b.compareTo(a);});
        //但是实际上还可以写得更短，如下： 对于函数体只有一行代码的，你可以去掉大括号{}以及return关键字，但是你还可以写得更短点
        names = Arrays.asList("xiaoming", "wanggang", "lilei");
        Collections.sort(names, (String a, String b)-> b.compareTo(a));
        System.out.println(names);
        //还可以更短，Java编译器可以自动推导出参数类型，所以你可以不用再写一次类型。
        names = Arrays.asList("xiaoming", "wanggang", "lilei");
        Collections.sort(names, (a, b)-> b.compareTo(a));
        System.out.println(names);
    }

    public class Student {
        private int age;
        private int name;

        public Student(int age, int name) {
            this.age = age;
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public int getName() {
            return name;
        }
    }
}
