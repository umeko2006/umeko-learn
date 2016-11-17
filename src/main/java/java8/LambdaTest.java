package java8;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.BinaryOperator;

/**
 * Created by yingmei.qym on 2016/11/16.
 */

public class LambdaTest {
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
    public void useLambda(){
        Runnable runnable = ()-> System.out.println("using lambda");
        runnable.run();

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
