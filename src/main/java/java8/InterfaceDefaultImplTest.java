package java8;

import org.junit.Test;

/**
 * Created by yingmei.qym on 2016/11/17.
 */
public class InterfaceDefaultImplTest {
    //一、接口的默认方法，也就是接口中可以有实现方法
    //以前的版本定义接口是不能有实现机制的，现在这样用了一个default关键字后，就可以实现，
    // 然后子类可以重写，也可以直接使用了。好处多多，感觉有点抽象类了...越来越灵活了
    @Test
    public void test(){
        MyInterface myInterface = new MyImpl();
        myInterface.doSomething();
        myInterface.doSomeMore();
        myInterface = new MyImpl2();
        myInterface.doSomething();
        myInterface.doSomeMore();
    }
    public interface MyInterface {
        void doSomething();
        default void doSomeMore(){
            System.out.println("do some more in interface");
        }
    }

    public class MyImpl implements MyInterface{
        @Override
        public void doSomething() {
             System.out.println("do something in impl");
        }

        @Override
        public void doSomeMore() {
             System.out.println("do some more in impl");
        }
    }

    public class MyImpl2 implements MyInterface{
        @Override
        public void doSomething() {
             System.out.println("do something in impl");
        }
    }
}
