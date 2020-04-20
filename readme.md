# J.U.C 并发工具包

## 并发工具类分类

- 并发安全

  - 从底层原理分类
    - 互斥同步
      - 使用互斥同步锁
        - synchronized
        - ReentrantLock
        - ReadWriteLock
        - ......
      - 使用同步工具类
        - Collections.synchronizedList(new ArrayList<E>()) 等
        - Vector等
    - 非互斥同步
      - atomic原子类
    - 结合互斥和非互斥同步
      - ConcurrentHashMap集合了synchronized和CAS
      - CopyOnWriteArrayList
      - 并发队列
        - 阻塞队列
          - ArrayBlockingQueue
          - LingkedBlockingQueue
          - ......
        - 非阻塞队列
          - ConcurrentLinkedQueue
      - ConcurrentSkipListMap 和ConcurrentSkipListSet
    - 无同步方案
      - ThreadLocal
      - final
  - 从使用者角度考虑
    - 避免共享变量
      - 线程封闭 ThreadLocal
      - 栈封闭 方法内部定义变量
    - 共享变量，但加以限制
      - final关键字
      - 使用互斥同步
    - 使用成熟的工具类
      - 并发集合和队列
      - atomic包下的原子类

- 管理线程、提高效率

  - 线程池
    - Executor
    - Executors
    - ExecutorService
    - 常见线程池
      - FixedThreadPool
      - CachedThreadPool
      - ScheduledThreadPool
      - SingleThreadPool
      - ForJoinPool
      - .......
  - 获取子线程的运行结果
    - Callable
    - Future
    - FutureTask

- 为了线程之间配合，来满足业务逻辑 AQS

  - CountDownLatch
  - CyclicBarrier
  - Semaphore
  - Condition
  - Exchanger
  - Phaser
  - ......

  

## 线程池

- 线程池的自我介绍

  - 软件中的“池”，可以理解为计划经济【资源有限，只能创建10个线程】，达到线程的复用和资源的控制
  - java 中的线程是有上限的，因为cpu和内存是有限的
  - 反复创建线程的开销比较大
  - 过多的线程会占用太多的内存
  - 使用线程池的场景
    - 服务器接收请求 ，比如tomcat
    - 开发中需要创建5个以上的线程

- 创建和停止线程池

  - 创建线程池

    - 构造函数方式

      | 参数          | 类型                     | 含义                                                        |
      | ------------- | ------------------------ | ----------------------------------------------------------- |
      | corePoolSize  | int                      | 核心线程数                                                  |
      | maxPoolSize   | int                      | 最大线程数                                                  |
      | keepAliveTime | int                      | 保持存活时间                                                |
      | workQueue     | BlokingQueue             | 任务存储队列                                                |
      | threadFactory | ThreadFactory            | 当线程池需要新的线程时候，会使用threadFactory来生成新的线程 |
      | Handler       | RejectedExecutionHandler | 由于线程池也无法接受你所提交的任务的拒绝策略                |

    - 构造函数参数详解

      - corePoolSize

        - 指的是核心线程数：线程池在完成初始化之后，默认情况下，线程池中并没有任何线程，线程池会等待有任务来的时候，再创建新线程去执行任务

      - maxPoolSize

        - 线程池有可能会在核心线程数的基础上，额外增加一些线程，但是这些新增的线程也有一个上限，这就是最大量maxPoolSize

        > 如果线程数小于corePoolSize，即使其他工作线程处于空闲状态，也会创建一个新线程来完成任务
        >
        > 如果线程等于或大于corePoolSize但少于maxPoolSize，会优先放入任务队列中
        >
        > 如果队列已经满了，并且线程数小于maxPoolSize，则创建新的线程来运行任务
        >
        > 如果队列已经满了，并且线程数大于或等于maxPoolSize，则拒绝该任务

        - 图示

          ![](线程池构造函数线程大小详解.png)

      - 增减线程池的特点

        - 通过设置corePoolSize和maxinumPoolSize相同，就可以创建固定大小的线程池
        - 线程池希望保持较少的线程数，并且只有在负载变得很大的时候才增加它
        - 通过设置maxinumPoolSize为很高的值，例如：Integer.MAX_VALUE，可以允许线程池容纳任意数量的并发任务
        - 只有队列满的时候才创建多余corePoolSize的线程，所以如果你使用的是无界队列（例如LinkedBlockingQueue），那么线程数就不会超过corePoolSize

    - keepAliveTime

      - 如果线程池当前的线程数多于corePoolSize，那么如果多余的线程空闲时间超过keepAliveTime，他们就会被终止

    - ThreadFactory

      - 新的线程由ThreadFactory创建，默认使用Executors.defaultThreadFactory()，创建出来的线程都在同一个线程组，拥有同样的NORM_PRIORITY优先级并且都不是守护线程，如果自己指定ThreadFactory,就可以改变线程名，线程组名，优先级，是否守护线程等

  - 手动创建和自动创建

  - 线程池里的数量如何设定

  - 停止线程的正确方法

- 常见线程池特点和用法

- 任务太多，怎么拒绝

- 钩子方法，给线程池加点料

- 实现原理，源码分析

- 使用线程池的注意点



## CAS原理

- 什么是CAS

  - 在并发过程中实现不能被打断的交换操作，我认为V的值应该是A，如果是的话我就把它改成B，如果不是A（说明被人修改了），那么我就不修改了，避免多人同时修改导致出错

  - CAS有三个操作数：内存值V，预期值A，要修改的值B，当且仅当预期值A和内存值V相等的时候，才将内存值改为B，否则什么都不做，最后返回现在的V值

  - 利用CPU的特殊指令

  - CAS的等价代码

    ```java
    package cas;
    
    /**
     * 模拟cas操作，等价代码
     */
    public class TwoThreadCompetition implements Runnable{
    
        private volatile int value;
    
    
        /**
         * 相当于CAS的指令
         * @param expectedValue
         * @param newValue
         * @return
         */
        public synchronized int compareAndSwap(int expectedValue,int newValue){
            int oldValue = value;
            if (oldValue == expectedValue){
                value = newValue;
            }
            return value;
        }
    
        public static void main(String[] args) throws InterruptedException {
            TwoThreadCompetition r = new TwoThreadCompetition();
            r.value = 0;
            Thread t1 = new Thread(r);
            Thread t2 = new Thread(r);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(r.value);
        }
    
        @Override
        public void run() {
            compareAndSwap(0,1);
        }
    }
    
    ```

    

- 案例演示

- 应用场景

- 以AtomicInteger为例，分析java中是如何利用cas实现原子操作

- 缺点

## AQS原理

- 学习AQS的思路
- 为什么要AQS
- AQS的作用
- AQS的重要性和地位
- AQS的内部原理
- 应用实例，源码解析
- 用AQS实现自己的Latch【门闩】





