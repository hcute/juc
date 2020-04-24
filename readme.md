#   J.U.C 并发工具包

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
      
    - WorkQueue

      - 任务简单，直接交接：SynchronousQueue，没有容量
      - 无界队列：LinkedBlockingQueue
      - 有界队列：ArrayBlockingQueue

  - 手动创建和自动创建

    - 手动创建更好，可以让我们更加明确线程池的运行规则，避免资源耗尽的风险
      - 根据不同的业务场景，自己设置线程池参数，我们的内存有多大，我们想给线程取什么名字等等
    - 自动创建线程池带来的问题
      - 都有可能带来oom错误，要么是队列是无界的，要么是maxPoolSize为Integer.MAX_VALUE

  - 线程池里的数量如何设定

    - 线程池的数量应该设置为多少比较合适
      - cpu密集型（比如加密、计算hash等）
        - 最佳线程数为cpu核心数的1-2倍左右
      - 耗时IO（读取数据库、文件、网络读写等）
        - 最佳线程数一般大于cpu核心数很多倍，以JVM线程监控显示繁忙情况为依据，保证线程空闲可以衔接上，参考Brain Goetz推荐的设计算法 
          - 线程数=CPU核心数*（1 + 平均等待时间/平均工作时间）

  - 停止线程的正确方法

    - shutdown 
      - 只是告诉线程池我想要停止，如果再有任务进来就不接受了，但是原来的任务会执行完
    - isShutdown
      - 是否收到了shutdown的信号
    - isTerminated
      - 是否执行完毕了
    - awaitTermination
      - 在一段时间内是否可以停止
      - 在以下情况下返回true
        - 所有任务都执行完毕
        - 等待的时间到了
        - 等待的过程中发生错误了
    - shutdownNow
      - 立即关闭线程池，但是正在执行的线程会发送interrupt ，而等待队列直接返回

- 常见线程池特点和用法

  - FixedThreadPool
    - 线程池里的线程大小是固定的【最大和核心相等，keepAliveTime=0】，但是采用了**LinkedBlockingQueue**，如果线程的任务很耗时，可能造成oom
  - CachedThreadPool
    - 线程池的大小是0，最大为**Integer.MAX_VALUE**，**keepAliveTime=60s**，采用**SynchronousQueue**，直接交换队列，可以进行回收，但是当任务过多时，也可能造成oom
  - ScheduledThreadPool
    - 核心线程数用户给出，最大线程个数为**Integer.MAX_VALUE**，**keepAliveTime=0**，采用**DelayedWorkQueue**，定时或者周期性执行任务，也可能造成oom
  - SingleThreadPool
    - 始终只有一个线程【最大和核心相等，keepAliveTime=0】，但是采用了**LinkedBlockingQueue**，线程任务耗时的时候，也可能造成oom
  - workStealingPool  
    - 有子任务的任务采用此线程池
    - 窃取
      - 各个线程下的子线程都在自己的队列里，如果其他线程的子线程完成了本职工作，可以帮其他线程完成任务
    - 弊端
      - 不保证执行顺序，最好不加锁

- 任务太多，怎么拒绝

  - 拒绝的时机
    - 我想停止了，但是你还提交任务，我就会拒绝
    - 以及当最大线程和工作队列的容量都满了，就会拒绝
  - 拒绝策略
    - AbortPolicy：直接抛出异常
    - DiscardPolicy：默默的把任务丢弃
    - DiscardOldestPolicy：丢弃很老的任务
    - CallerRunsPolicy：谁提交任务，谁来帮我执行
      - 避免了任务丢弃
      - 降低任务提交的速度

- 钩子方法，给线程池加点料

  - 每个任务执行之前和之后做些事情

    - 日志和统计

    - 暂停线程

      ```java
      package threadpool;
      
      import jdk.nashorn.internal.ir.CallNode;
      
      import java.util.concurrent.*;
      import java.util.concurrent.locks.Condition;
      import java.util.concurrent.locks.Lock;
      import java.util.concurrent.locks.ReentrantLock;
      
      /**
       * 演示每个任务执行的前后都执行钩子函数
       */
      public class PauseableThreadPool extends ThreadPoolExecutor {
      
          private boolean isPause;
          private final Lock lcok = new ReentrantLock();
          private Condition unpaused = lcok.newCondition();
      
          public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
              super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
          }
      
          public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
              super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
          }
      
          public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
              super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
          }
      
          public PauseableThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
              super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
          }
      
          @Override
          protected void beforeExecute(Thread t, Runnable r) {
              super.beforeExecute(t, r);
              lcok.lock();
              try {
                  while (isPause) {
                      unpaused.await();
                  }
              } catch (InterruptedException e) {
                  e.printStackTrace();
              } finally {
                  lcok.unlock();
              }
          }
      
          @Override
          protected void afterExecute(Runnable r, Throwable t) {
              super.afterExecute(r, t);
          }
      
          private void pause(){
              lcok.lock();
              try {
                  isPause = true;
              } catch (Exception e) {
                  e.printStackTrace();
              } finally {
                  lcok.unlock();
              }
          }
      
          public void resume(){
              lcok.lock();
              try {
                  isPause = false;
                  unpaused.signalAll();
              } catch (Exception e) {
                  e.printStackTrace();
              } finally {
                  lcok.unlock();
              }
          }
      
          public static void main(String[] args) throws InterruptedException {
              PauseableThreadPool pauseableThreadPool = new PauseableThreadPool(10, 20, 10l, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
      
              Runnable r = new Runnable() {
                  @Override
                  public void run() {
                      System.out.println("我被执行了");
                      try {
                          Thread.sleep(10);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
              };
              for (int i = 0; i < 10000; i++) {
                  pauseableThreadPool.execute(r);
              }
              Thread.sleep(1500);
              pauseableThreadPool.pause();
              System.out.println("线程池被暂停了");
              Thread.sleep(1500);
              System.out.println("线程池被恢复了");
              pauseableThreadPool.resume();
      
      
          }
      }
      
      ```

      

- 实现原理，源码分析

  - 线程池的组成部分

    - 线程池管理器
      - 创建线程池，停止线程池
    - 工作线程
      - 执行任务的线程
    - 任务队列
      - 存放不能及时处理的任务，必须是线程安全的队列，因为多个线程都需要去取任务
    - 任务接口（Task）
      - 线程具体执行的run方法

  - 线程池、ThreadPoolExecutor、ExecutorService、Executor、Executors等类之间的关系

    ![](线程池类之间的关系.png)

    - Executors是一个工具类

  - 线程池是如何实现线程复用的

    - 相同的线程执行不同的任务

  - 线程池的状态

    - Running
      - 接收新任务并处理排队任务
    - shutdown
      - 不接收新任务，但处理排队任务
    - stop
      - 不接收新任务，也不处理排队任务，并中断正在进行的任务
    - tidying
      - 所有的任务都已终止，workCount为0，线程会转换到TIDYING状态，并将运行teminate()钩子方法
    - terminated
      - terminate方法运行完成

- 使用线程池的注意点

  - 避免任务的堆积
  - 线程的过度增加
  - 排查线程泄漏：线程执行完毕，但是无法回收



## ThreadLocal

### 使用场景

- 每个线程需要一个独享的对象（通常是工具类，典型需要使用的类有SimpleDateFormat和Random）
  - 使用initialValue，在第一次使用get的时候就把对象初始化出来，对象的初始化时机可以由我们控制
- 每个线程内需要保存一些全局变量（例如拦截器中获取用户信息），可以让不同的方法直接使用，避免参数传递的麻烦
  - 使用set，对象的生成时机不由我们控制

### 作用

- 可以让对象在线程中隔离
- 可以在同一个线程中的任何方法都可以获取到对象



### 好处

- 线程安全
- 不需要加锁，执行效率提高
- 更高效的利用内存，节省开销
- 避免传参的麻烦



### ThreadLocal 详解

- 原理，源码解析

> - Thread、ThreadLocal、ThreadLocalMap之间的关系
>   - 每个Thread中都有一个ThreadLocalMap 成员变量，每一个ThreadLocalMap中存放多个ThreadLocal
>
> - initialValue方法
>   - 该方法会返回当前线程对应的“初始值”，这是一个延时加载的方法，只有在调用了get方法的时候，才会触发
>   - 当线程第一次调用get方法访问变量的时，将调用此方法，除非线程调用了set方法，则不会调用initialValue方法
>   - 每个线程最多调用一次initialValue方法，如果调用了remvoe方法，则需要再调用一次initialValue方法
>   - 如果不重写次方法，返回的是null，一般使用匿名内部类的方式重写initialValue方法
> - set方法
>   - 给这个线程设置一个新值
> - get方法
>   - 得到这个线程对应的value，如果是首次调用get，则会调用initialValue来得到这个值
>   - **先取出当前线程的ThreadLocalMap，然后调用map.getEntry方法，把本地ThreadLocal的引用传入，取出map中属于ThreadLocal的Value**
> - remove方法
>   - 删除线程保存的值
> - **Thread类中的ThreadLocalMap 会被以下方法初始化**
>   - ThreadLocal的set方法
>   - ThreadLocal的setInitialValue方法



-  ThreadLocal.ThreadLocalMap
  - key为ThreadLocal
  - value为自己存储的对象
  - hash冲突，java8 会变成链表，大于8 变成红黑树，但是ThreadLocalMap不是这种方式
- 两种使用场景的相同
  - 最后都是调用ThreadLocalMap色map.set()方法设置ThreadLocal和value
  - 只是入口不同，一个是set一个是initialValue

### ThreadLocal注意点

- 内存泄漏
  - 某个对象不再使用了，但是占用的内存却不能被回收
  - 发生的可能性
    - ThreadLocalMap的Entry出初始化key的初始化调用了WeakReference进行初始化，使用了弱引用，看到弱引用垃圾回收肯定会回收，但是value是强饮用
      - 如果使用了线程池，线程不会被终止，那么ThreadLocalMap就一直存在，但是key是弱引用，value是强引用，导致key 被回收，value没有被回收，此时就存在大量无用的value值
      - 但是jdk在set，remove，rehash方法中会扫描key为null的Entry，并把value设置为null，这样value也就可以被回收了，所以我们需要调用这些方法
  - **如何避免【阿里规约】**
    - 使用完了ThreadLocal，就需要调用remove方法
- 空指针异常
  - get方法不会报nullPointException，只有自己使用get后的操作会报npe
- 共享对象
  - 如果放进去的是共享对象，比如static修饰的，同样会存在线程安全问题，不应该放共享对象
- 如果不需要使用ThreadLocal就不用
- 优先使用框架的支持
  - Spring
    - RequestContextHolder
    - DateTimeContextHolder
  - http请求是一个线程，相互独立，适合用ThreadLocal



## 锁

### Lock接口

- 简介、地位和作用
  - 是一种工具，对共享资源访问进行控制
  - Lock 和 Synchronized，这是两个常见的锁，都能达到线程安全，但是使用的方法和功能上差别比较大
  - Lock并不是来代替synchronized的，而是当使用synchronized不适合或不满足要求的时候，来提供高级功能
  - Lock最长见的实现类ReentrantLock
  - 通常情况下，Lock只允许一个线程来访问这个共享资源，不过有些时候，一些特殊的实现可以允许并发访问，比如ReadWriteLock里的ReadLock
- 为什么synchronized不够用，为什么需要lock
  - synchronized 效率比较低，锁释放的情况比较少（执行代码完成），试图获得锁时不能设定超时，不能中断一个正在试图获取锁的线程
  - synchronized 不够灵活（读写锁更灵活）：加锁和释放时机单一，每个锁仅有单一的条件（某个对象），可能是不够的
  - synchronized  无法知道是否成功获取到了锁
- 方法介绍
  - 常见方法
    - lock
      - 获取锁，如果锁被其他线程获取，则进行等待
      - 和synchronzied不一样，异常时不会释放锁，必须放在finally里释放锁
      - lock方法不能被中断，这会带来很大的隐患，一旦陷入死锁，lock就会永久等待
    - trylock
      - 尝试获取锁，当前锁被其他线程占有，则获取失败返回false，没有被占有返回true
      - 相对于lock，更有优势，可以根据是否获取到锁来判定是否执行后面的代码
      - 会立刻返回，不会一直等待
    - tryLock(long time,TimeUnit unit)
      - 在一段时间内返回true 和false
    - tryInterruptibly()
      - 相当于tryLock(long time,TimeUnit unit)把超时时间设置为无限，在等待锁的过程中，线程可以被中断
    - unlock
      - 获取到锁立马写释放锁的代码
- 可见性保证
  - 和synchronized 有同样的内存模型

### 锁的分类



#### 乐观锁和悲观锁

**从线程要不要锁住同步资源来分类，乐观锁又称之为非互斥同步锁，悲观锁又称之为互斥同步锁**

- 为什么会诞生非互斥同步锁 - 互斥同步锁的劣势

  - 互斥同步锁【synchronized和Lock相关】
    - 阻塞和唤醒带来的性能的问题
    - 永久阻塞：如果持有锁的线程永久阻塞，比如遇到了死循环，死锁等活跃性问题，那么等待同一把锁的线程，也将永远得不到执行
    - 优先级反转，阻塞优先级比较高，但是拥有锁的线程优先级比较低，此时拥有锁的线程如果不释放锁，那么等锁的线程的优先级没有意义

- 什么是乐观锁和悲观锁

  - 悲观锁：操作对象之前总是先锁住操作的对象不让别的线程来操作该对象
    - synchronized和Lock相关
  - 乐观锁：
    - 认为自己处理对象的时候其他线程不会来干扰，所以不会锁住被操作的对象
    - 更新的时候，去比对我修改的期间数据有没有被其他人修改，如果没被修改过，就说明只有自己在操作，那么就正常的修改数据
    - 如果数据和我一开始拿到的不一样了，我就认为有人在修改数据，我就放弃修改，采取报错和重试的策略
    - 一般都是利用CAS算法来实现的
    - 典型例子：原子类和并发容器等

- 典型的例子

  - 乐观锁
    - 原子类和并发容器
    - git 版本管理器push的时候
    - update set num = 2,version = version + 1 where version = 1 and id =5;
  - 悲观锁
    - synchronized和Lock
    - select for update 就是悲观锁

- 开销对比

  - 悲观锁的开销要高于乐观锁，但是特点是一劳永逸，临界区持锁时间就越来越差，也不会对互斥锁的开销产生影响
  - 乐观锁一开始的开销比悲观小，但是如果自旋时间很长或者不停重试，那么消耗的资源也就随之增多

- 两种锁的使用场景

  - 悲观锁：适用于并发写入很多的情况，适用于临界区持锁时间比较长的情况，可以避免大量的无用自旋等待等消耗
    - 临界区有IO操作
    - 临界区代码复杂或者循环量很大
    - 临界区竞争非常激烈
  - 乐观锁：适合并发写入少，大部分是读取的场景，不加锁能让读取大幅度的提高性能

  

#### 可重入锁和非可重入锁【ReentrantLock 】

**同一个线程是否可以重复获取同一把锁**

- 什么叫可重入 
  - 同一个线程下可以多次获取同一把锁
- 好处
  - 避免死锁
  - 提升了封装性 
- 非不可重入
  - ThreadPoolExecutor的Worker类
- AQS的应用
- ReentrantLock的其他常用方法
  - isHeldByCurrentThread 查看锁是否被当前线程持有
  - getQueueLength 返回当前正在等待这把锁的队列有多长



#### 公平锁和非公平锁

**多线程竞争的时候是否需要排队**

- 什么是公平什么是非公平

  - 按照线程的请求的顺序，来分配锁，非公平指的是，不完全按照请求的顺序，在一定情况下可以插队
  - 非公平锁也是不提倡插队行为的，这里的非公平，指的是“在合适的时机”插队，而不是盲目插队
  - 什么是合适时机
    - 买火车票插队 
      - 以前是线下排队，比如排在我前面的还有一个人，买好票走了，轮到我了，我熬夜后有点懵一时没反应过来，这个时候有个人来问了下火车几点开，但是他没有买票就走了，其实没有影响我买到票

- 为什么有非公平的存在

  - 避免线程在阻塞切换到唤醒的过程的得到利用，比如A线程拿到锁，B线程在阻塞状态等待锁，此时A释放了锁，又一个C线程在执行过程中需要获取锁，B在醒来过程中准备抢锁，这个时候是准许C先抢到锁的，达到双赢

- ReentrantLock的公平情况

  - 默认是**非公平锁**，在创建的时候传入true 就是公平锁
  - 按照排队队列来分配锁

- ReentrantLock的非公平情况

  - 排队的队列中的线程，和还没有进入到队列中的线程同时抢锁，此时会先给没有进入队列线程分配锁，此时就是公平锁

- 代码案例，演示公平和非公平

  ```java
  package lock.reentrantlock;
  
  import java.util.Random;
  import java.util.concurrent.locks.Lock;
  import java.util.concurrent.locks.ReentrantLock;
  
  /**
   * 演示公平和非公平锁
   */
  public class FairLock {
  
  
  
      public static void main(String[] args) {
          PrintQueue printQueue = new PrintQueue();
          Thread thread[] = new Thread[10];
          for (int i = 0; i < 10; i++) {
              thread[i] = new Thread(new Job(printQueue));
          }
  
          for (int i = 0; i < 10; i++) {
              thread[i].start();
              try {
                  Thread.sleep(100);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
      }
  }
  
  class Job implements Runnable {
      private PrintQueue printQueue;
  
      public Job(PrintQueue printQueue) {
          this.printQueue = printQueue;
      }
  
      @Override
      public void run() {
          System.out.println(Thread.currentThread().getName() + "开始打印");
          printQueue.printJob(new Object());
          System.out.println(Thread.currentThread().getName() + "打印完毕");
      }
  }
  
  class PrintQueue{
  //    private Lock queueLock = new ReentrantLock(true); // 公平
  
      private Lock queueLock = new ReentrantLock(false); // 非公平
  
      public void printJob(Object document) {
          queueLock.lock();
          try {
              int duration =  new Random().nextInt(10) + 1;
              System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration + "秒");
              Thread.sleep(duration*1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          } finally {
              queueLock.unlock();
          }
          queueLock.lock();
          try {
              int duration =  new Random().nextInt(10) + 1;
              System.out.println(Thread.currentThread().getName() + "正在打印，需要" + duration + "秒");
              Thread.sleep(duration*1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          } finally {
              queueLock.unlock();
          }
      }
  }
  
  ```

  

- 特例

  - trylock方法，不遵守设定的公平原则，当有线程只从trylock的时候，一旦有线程释放了锁，那么这个正在trylock的线程就能获取到锁，即使它之前已经有其他线程在排队等锁

- 公平和非公平的优缺点

  - 公平锁
    - 优势：各个线程公平平等，每个线程在等待一段时间后，总有执行的机会
    - 劣势：更慢，吞吐量小
  - 非公平锁
    - 优势：更快，吞吐量更大
    - 劣势：有可能产生线程饥饿，也就是某个线程长时间内始终得不到执行

- 源码是如何实现

  - 公平锁会判断队列



#### 共享锁和排他锁

**从多线程能否共享同一把锁**

- 排它锁 synchronized
  - 又称独占锁、独享锁
- 共享锁
  - 读锁，获取到共享锁之后，可以查看但是无法修改和删除数据，其他线程此时也可以获取到共享锁，可以查看但是无法修改数据
- 典型实例
  - ReentrantReadWriteLock中的读锁是共享锁，写锁是排它锁
- 读写锁的作用 多读一写
  - 只是读的话没有必要加ReentrantLock，只用加锁
  - 多个线程同时申请读锁，可以申请到
  - 如果一个线程占用了读锁，此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁
  - 如果一个线程占用了写锁，其他线程想要获取读锁和写锁都获取不到 
- 读锁和写锁的交互过程
  - 选择规则
    - 选择队列中的什么线程获取锁
  - 读线程插队 不准读锁插队
    - 公平的 ReentrantReadWriteLock 不准读锁插队
    - 非公平 ReentrantReadWriteLock 也不准读锁插队，写锁可以随时插队
      - 场景：2和4线程在读，3想写进入等待，5过来想读
        - 如果准许读插队：后面还来很多读的线程，那么写锁永远都获取不到，造成了**饥饿**
        - 如果不准许插队：进入队列队列
      - 读锁仅在等待队列头节点不是想获取写锁的线程的时候可以插队
  - 升降级
    - 支持锁的降级，不支持升级
      - 持有写锁获取读锁，释放写锁 可以
      - 持有读锁升级到写锁 不可以
    - 为什么不支持锁的升级
      - 会造成死锁，只有读锁的释放了，才可以升级为写锁
- 使用场景
  - 读多写少的场景



#### 自旋锁和阻塞锁

**等锁的过程，如果等锁的过程一直尝试再去获取锁，就是自旋锁，如果等锁的时候阻塞，就是阻塞锁**

- 什么是自旋
  - 不使用自旋，那么就只能阻塞和等待来处理线程之间的协调，阻塞或唤醒一个java线程需要操作系统切换cpu状态来完成，这种状态的切换需要耗费处理器时间
  - 如果同步代码内容很简单，状态切换消耗的时间有可能比用户代码执行时间还要长，就不需要阻塞或等待
  - 为了应对资源同步时间短的情况，我们就可以让后面那个请求锁的线程不放弃cpu的执行时间，看看持有锁的线程是否很快就会释放锁
  - 为了当前等待锁的线程稍微等下，需要当前线程自旋，当持有锁的资源放弃了锁，那么就不需要阻塞直接获取锁，从而避免了切换线程的开销，这就是自旋
- 自旋的缺点
  - 如果锁的占用时间过长，那么自旋的线程只会白浪费处理器资源，自旋过程中一直消耗cpu，虽然开始的开销低，但是随着自旋时间的增长，开销也会增大
- 原理和源码分析
  - atomic 用到了自旋锁，实现原理里是CAS，AtomicInteger中调用了unsafe进行自增操作的源码中的do-while循环就是一个自旋操作，如果修改过程中遇到其他线程竞争导致中没有修改成功，就在while里死循环，直至修改成功
- 使用场景
  - 自旋锁一般用于多核的服务器，在并发度不是特别高的情况下，比阻塞所的效率高
  - 使用于临界区比较小的情况

#### 可中断锁和非可中断锁

- 可中断锁
  - lock是可中断的，因为trylock(time) 和lcokInterruptibly都能响应中断，而synchronized是不可中断的
  - 可中断就是，如果线程A正在执行锁中的代码，另外一个线程B正在等待获取锁，可能由于长时间等待，线程B不想等了，就先处理其他事情，我们可以中断它，这就是可中断锁

### 锁优化

- jvm优化

  - 自旋锁和自适应
    - 尝试10-20次之后可能就会转换为阻塞锁，可以在jvm参数重配置自旋的次数，这就是自适应
  - 锁消除
    - 如果所有的变量都是局部的，即使你加锁了jvm也会消除锁
  - 锁粗话
    - 如果你在太多的代码片段都加锁了，jvm发现可以合并就会合并锁

- 自己优化

  - 缩小同步代码块

  - 尽量不要锁住方法

  - 减少锁的请求次数

  - 避免人为制造热点

  - 锁中尽量不要包含锁 

  - 选择合适的锁和合适的工具类

    









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





