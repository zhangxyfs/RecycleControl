## RecycleView控制器
#### 说明：非侵入式的recyclerView 控制器。可以对带有SwipeRefreshLayout 和 recyclerView 页面进行下拉刷新和上拉加载<br />
#### 使用了第三方库：
```
dependencies {
		'io.reactivex.rxjava2:rxjava:2.x.y'
		'io.reactivex.rxjava2:rxandroid:2.0.1'
}
```
##### 调用：
```java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerControl mRecyclerControl = new RecyclerControl(mSwipeRefreshLayout, mLinearLayoutManager, this);
        mRecyclerControl.setSwipeRefreshLayoutEnable(false);//设置是否可以手动下拉刷新和上拉加载
    }
```
##### 简单说下如何提交到jcenter：
###### https://bintray.com/signup/oss 一定要在这里注册账号！！
###### https://bintray.com/signup/oss 一定要在这里注册账号！！
###### 重要的事情说两遍！！
其他的按照bintrayUpload1.gradle里头的内容写就行了。

ps：因为注册的账号类型不对折腾了3天，唉..
