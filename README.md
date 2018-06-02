# showFake3D
安卓伪3D商品展示效果实现，模仿超真实<br>
[展示](https://blog.csdn.net/b805887485/article/details/80546351)
```
maven { url 'https://jitpack.io' }

implementation 'com.github.BaojunCZ:showFake3D:v1.1'
```
一共三个构造方法<br>
## 完整构造
init(ArrayList<String> pics, int moveOffset, int intertiaStart, int intertiaOffset, int intertiaEnd)<br>
一参图片地址集合，图片是已从右往左为基准，二参是滑动整个宽度切换多少张图片，三四五参是惯性相关参数，分别是惯性开始速度，衰弱速度，结束速度<br>
## 其他构造
init(ArrayList<String> pics, int moveOffset)<br>
init(ArrayList<String> pics)<br>

## 监听方法
```
setCacheListener(onCacheFinishListener cacheListener) 
```
缓存结束后的回调

## 注意
1图片地址参照fresco的拼装方式[查看](https://www.fresco-cn.org/docs/supported-uris.html)<br>
2图片顺序为从右向左滑动方向

## demo
```
<baojun.com.fake3dview.Show3DView
        android:id="@+id/show3DView"
        android:layout_width="250dip"
        android:layout_height="250dip" />
```	

```	
show3DView = findViewById(R.id.show3DView);
show3DView.init(getImages(), 15, 10, 2, 100);
show3DView = findViewById(R.id.show3DView);
        show3DView.init(getImages(), 15, 10, 2, 100);
        show3DView.setCacheListener(new Show3DView.onCacheFinishListener() {
            @Override
            public void finish() {
                Toast.makeText(MainActivity.this, "缓存结束", Toast.LENGTH_LONG).show();
            }
        });
```

```
private ArrayList<String> getImages() {
        ArrayList<String> pics = new ArrayList<>();
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic1);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic2);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic3);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic4);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic5);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic6);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic7);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic8);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic9);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic10);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic11);
//        pics.add("res://" + getPackageName() + "/" + R.drawable.pic12);
        pics.add("http://www.slys.ml/image/pic1.jpg");
        pics.add("http://www.slys.ml/image/pic2.jpg");
        pics.add("http://www.slys.ml/image/pic3.jpg");
        pics.add("http://www.slys.ml/image/pic4.jpg");
        pics.add("http://www.slys.ml/image/pic5.jpg");
        pics.add("http://www.slys.ml/image/pic6.jpg");
        pics.add("http://www.slys.ml/image/pic7.jpg");
        pics.add("http://www.slys.ml/image/pic8.jpg");
        pics.add("http://www.slys.ml/image/pic9.jpg");
        pics.add("http://www.slys.ml/image/pic10.jpg");
        pics.add("http://www.slys.ml/image/pic11.jpg");
        pics.add("http://www.slys.ml/image/pic12.jpg");
        return pics;
    }
```
