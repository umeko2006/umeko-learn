package memory;

/**
 * Created by yingmei.qym on 2016/10/21.
 */
public class MemoryModelTest {

    /*
    用该方法演示java.lang.StackOverflowError。

    虚拟机栈 (Java Virtual Machine Stacks)是线程私有的，与线程在同一时间创建。管理JAVA方法执行的内存模型。
    每个方法执行时都会创建一个桢栈来存储方法的私有变量、操作数栈、动态链接方法、返回值、返回地址等信息。
    栈的大小决定了方法调用的可达深度（递归多少层次，或嵌套调用多少层其他方法，-Xss参数可以设置虚拟机栈大小）。
    栈的大小可以是固定的，或者是动态扩展的。
    如果栈的深度是固定的，请求的栈深度大于最大可用深度，则抛出stackOverflowError；
    如果栈是可动态扩展的，但没有内存空间支持扩展，则抛出OutofMemoryError。

     */
    public static void main(String [] args){
        compute(10000000);
    }


    public static void compute(Integer value){
//        System.out.println("compute begin");
        int endValue = 0;
        if (value == endValue){
            return;
        } else {
            compute(value - 1);
        }


    }
}
