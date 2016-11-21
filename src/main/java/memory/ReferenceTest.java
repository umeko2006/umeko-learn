package memory;

import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by yingmei.qym on 2016/11/21.
 */
public class ReferenceTest {
    public static boolean isRun = true;

    @Test
    public void strongReference() {
        Object referent = new Object();
        /**
         * 通过赋值创建 StrongReference
         */
        Object strongReference = referent;
        assertSame(referent, strongReference);
        referent = null;
        System.gc();
        /**
         * StrongReference 在 GC 后不会被回收
         */
        assertNotNull(strongReference);
    }

    @Test
    public void weakReference() {
        Object referent = new Object();
        WeakReference<Object> weakRerference = new WeakReference<Object>(referent);
        assertSame(referent, weakRerference.get());
        referent = null;
        System.gc();
        /**
         * 一旦没有指向 referent 的强引用, weak reference 在 GC 后会被自动回收
         */
        assertNull(weakRerference.get());
    }

    @Test
    public void phantomReference() throws Exception{
        /*
           虚顾名思义就是没有的意思，建立虚引用之后通过get方法返回结果始终为null,通过源代码你会发现,
           虚引用通向会把引用的对象写进referent,只是get方法返回结果为null.先看一下和gc交互的过程在说一下他的作用.
  1、不把referent设置为null, 直接把heap中的new String("abc")对象设置为可结束的(finalizable).
  2、与软引用和弱引用不同, 先把PhantomRefrence对象添加到它的ReferenceQueue中.然后在释放虚可及的对象.
   你会发现在收集heap中的new String("abc")对象之前,你就可以做一些其他的事情.通过以下代码可以了解他的作用.
         */

        String abc = new String("abc");
        System.out.println(abc.getClass() + "@" + abc.hashCode());
        final ReferenceQueue referenceQueue = new ReferenceQueue<String>();
        new Thread() {
            public void run() {
                while (isRun) {
                    /*
                    只有运行了System.gc();以后，jvm才会将虚引用放到referenceQueue中，
                     */
                    Object o = referenceQueue.poll();
                    System.out.println("running, o:" + o);
                    if (o != null) {
                        try {
                            Field rereferent = Reference.class
                                    .getDeclaredField("referent");
                            rereferent.setAccessible(true);
                            Object result = rereferent.get(o);
                            System.out.println("gc will collect:"
                                    + result.getClass() + "@"
                                    + result.hashCode() + ", string:" + result);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("object not enqueued");
                    }
                    try {
                        Thread.currentThread().sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        PhantomReference<String> abcWeakRef = new PhantomReference<String>(abc,
                referenceQueue);
        abc = null;
        System.out.println(abc);
        Thread.currentThread().sleep(3000);
        System.out.println("before gc");
        System.gc();
        System.out.println("after gc");
        Thread.currentThread().sleep(3000);
        isRun = false;
    }
}
