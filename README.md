## RecycleView控制器
#### 说明：非侵入式的recyclerView 控制器。可以对带有SwipeRefreshLayout 和 recyclerView 页面进行下拉刷新和上拉加载<br />
##### Download：
[ ![Download](https://api.bintray.com/packages/zxyjb212/maven/android_recycler_control/images/download.svg) ](https://bintray.com/zxyjb212/maven/android_recycler_control/_latestVersion)

Download the latest JAR or grab via Maven:
```xml
<dependency>
  <groupId>com.z7dream</groupId>
  <artifactId>android_recycler_control</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```
   compile 'com.z7dream:android_recycler_control:0.0.1'
```
#### 使用了第三方库：
```
dependencies {
		'io.reactivex.rxjava2:rxjava:2.x.y'
		'io.reactivex.rxjava2:rxandroid:2.0.1'
}
```
#### How do I use RecycleControl?
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerControl = new RecyclerControl(swipeRefreshLayout, linearLayoutManager, this);//初始化
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());//滚动监听

        handler = new Handler(Looper.myLooper());

    }

    @Override
    public void onControlGetDataList(final boolean isRef) {
        //todo get data
        handler.postDelayed(new Runnable() {//模拟刷新数据
            @Override
            public void run() {
                recyclerControl.getDataComplete(isRef);//刷新结束
            }
        }, 1000);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();//调用刷新球
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerControl.destory();
        recyclerControl = null;
    }
```
##### 简单说下如何提交到jcenter：
###### https://bintray.com/signup/oss 一定要在这里注册账号！！
###### https://bintray.com/signup/oss 一定要在这里注册账号！！
###### 重要的事情说两遍！！
其他的按照bintrayUpload.gradle里头的内容写就行了。
完成local.properties 的设置需要注意几个事情：

#项目名称<br />
project.name=android_recycler_control<br />
#项目的groupId(包名)<br />
project.groupId=com.z7dream<br />
#项目的artifactId，这里需要跟项目的library名称一致，不然传到bintray的包和名字对不上会找不到（和项目名一样）<br />
project.artifactId=android_recycler_control<br />
<br />

最后需要执行以下几个命令：
gradew javadocJar
gradew sourcesJar
gradew install
gradew bintrayUpload

ps：因为注册的账号类型不对折腾了3天，唉..
