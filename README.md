# ScaleViewPager
A custom ViewPager that can scale display on one page.

![ScaleViewPager](https://github.com/liuyuejinqiu/ScaleViewPager/blob/master/screenshot/sample.gif)

Use ScaleViewPager library
----------
 Gradle
```groovy
compile 'com.jinqiu:scaleviewpager:1.0.1'
```

 Maven
```groovy
<dependency>
  <groupId>com.jinqiu</groupId>
  <artifactId>scaleviewpager</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

 Ivy
```groovy
<dependency org='com.jinqiu' name='scaleviewpager' rev='1.0.1'>
  <artifact name='scaleviewpager' ext='pom' ></artifact>
</dependency>
```

Use ScaleRecyclerViewPager library
----------
 Gradle
```groovy
compile 'com.jinqiu:scalerecyclerpager:1.0.1'
```

 Maven
```groovy
<dependency>
  <groupId>com.jinqiu</groupId>
  <artifactId>scalerecyclerpager</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

 Ivy
```groovy
<dependency org='com.jinqiu' name='scalerecyclerpager' rev='1.0.1'>
  <artifact name='scalerecyclerpager' ext='pom' ></artifact>
</dependency>
```
Set attributes
----------
in code
```java
        //ScaleViewPager
        ScaleViewPager scaleViewPager = (ScaleViewPager) findViewById(R.id.scaleViewPager);
        scaleViewPager.setAdapter(new ViewPagerAdapter());
        scaleViewPager.setCoverWidth(40f);
        scaleViewPager.setMaxScale(1.0f);
        scaleViewPager.setMaxScale(0.9f);

        //ScaleRecyclerViewPager
        ScaleRecyclerViewPager scaleRecyclerViewPager = (ScaleRecyclerViewPager) findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        scaleRecyclerViewPager.setLayoutManager(layout);
        scaleRecyclerViewPager.setAdapter(new RecyclerLayoutAdapter(this, scaleRecyclerViewPager));
        scaleRecyclerViewPager.setHasFixedSize(true);
        scaleRecyclerViewPager.setLongClickable(true);
        scaleRecyclerViewPager.setCoverWidth(40f);
        scaleRecyclerViewPager.setMaxScale(1.0f);
        scaleRecyclerViewPager.setMaxScale(0.9f);
```
in XML
```xml

<!--<com.jinqiu.view.scaleviewpager.ScaleViewPager-->
  <com.jinqiu.view.scaleviewpager.ScaleViewPager
        android:id="@+id/scaleViewPager"
        android:layout_width="match_parent"
        android:layout_height="160dp"
        android:layout_centerInParent="true"
        android:layout_marginTop="10dp"
        android:paddingLeft="20dp"
        android:paddingRight="20dp"
        app:svp_maxScale="1.0"
        app:svp_coverWidth="20dp"
        app:svp_minScale="0.9" />

<!--<com.jinqiu.view.scalerecyclerpager.ScaleRecyclerViewPager-->
<com.jinqiu.view.scalerecyclerpager.ScaleRecyclerViewPager
        android:id="@+id/viewpager"
        android:layout_width="match_parent"
        android:layout_height="160dp"
        android:layout_centerInParent="true"
        android:layout_marginTop="10dp"
        android:paddingLeft="15dp"
        android:paddingRight="15dp"
        app:rvp_singlePageFling="true"
        app:rvp_triggerOffset="0.1"
        app:srp_coverWidth="20dp"
        app:srp_maxScale="1.0"
        app:srp_minScale="0.9" />
```
Other
----------

- Do not set the difference between `maxScale` and `minScale` too much.
- Do not set `mCoverWidth`,`paddingLeft`and`paddingRight`too large.
