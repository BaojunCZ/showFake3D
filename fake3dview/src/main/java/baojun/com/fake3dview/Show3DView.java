package baojun.com.fake3dview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;

/**
 * Created by 包俊 on 2018/5/31.
 */
public class Show3DView extends LinearLayout {

    private SimpleDraweeView iv;
    private int widthMeasureSpec;
    private float singleSpec;
    private String Tag = "Show3DView";
    private Handler handler = new Handler();
    private boolean positive = true;
    private boolean leftScreen = false;
    private ArrayList<String> pics;
    private int moveOffset, intertiaStart, intertiaOffset, intertiaEnd;
    private float start;
    private int NowID = 1;
    private long startTime;

    public Show3DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Fresco.initialize(context);
        LayoutInflater.from(context).inflate(R.layout.view_3d, this);
    }

    /**
     * 完整构造方法
     *
     * @param pics           图片集合
     * @param moveOffset     图片等分
     * @param intertiaStart  惯性开始速度
     * @param intertiaOffset 惯性间隔速度
     * @param intertiaEnd    惯性停止速度
     */
    public void init(ArrayList<String> pics, int moveOffset, int intertiaStart, int intertiaOffset, int intertiaEnd) {
        this.pics = pics;
        if (moveOffset > pics.size() * 2) {
            this.moveOffset = pics.size() * 2;
        } else {
            this.moveOffset = moveOffset;
        }
        this.intertiaStart = intertiaStart;
        this.intertiaOffset = intertiaOffset;
        this.intertiaEnd = intertiaEnd;
        initData();
    }

    /**
     * 完整构造方法
     *
     * @param pics       图片集合
     * @param moveOffset 图片等分
     */
    public void init(ArrayList<String> pics, int moveOffset) {
        this.pics = pics;
        if (moveOffset > pics.size() * 2) {
            this.moveOffset = pics.size() * 2;
        } else {
            this.moveOffset = moveOffset;
        }
        this.intertiaStart = 10;
        this.intertiaOffset = 2;
        this.intertiaEnd = 100;
        initData();
    }

    /**
     * 默认构造方法，只需要图片
     *
     * @param pics
     */
    public void init(ArrayList<String> pics) {
        this.pics = pics;
        this.moveOffset = 15;
        this.intertiaStart = 10;
        this.intertiaOffset = 2;
        this.intertiaEnd = 100;
        initData();
    }

    private void initData() {
        iv = findViewById(R.id.iv);
        setCache(0);
        ViewTreeObserver vto = iv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                widthMeasureSpec = iv.getMeasuredWidth();
                singleSpec = widthMeasureSpec / moveOffset;
//                Log.e(Tag, "widthMeasureSpec>>" + widthMeasureSpec);
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                leftScreen = false;
                start = event.getX();
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                float distance = event.getX() - start;
                distance = distance > 0 ? distance : -distance;
                long partTime = System.currentTimeMillis() - startTime;
                float v = distance / partTime;
                v = v > 0 ? v : -v;
                float StandardV = singleSpec / 60;
//                Log.e(Tag, "distance=" + distance + ">>singleSpec=" + singleSpec + ">>v=" + v + ">>StandardV=" + StandardV);
                if (distance < singleSpec) {
                    leftScreen = false;
                } else if (v < StandardV) {
                    leftScreen = false;
                } else {
                    leftScreen = true;
//                    Log.e(Tag, "ACTION_UP>>" + NowID);
                    intertia(NowID, intertiaStart);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getX();
                //计算移动距离
                float spec = nowX - start;
                int mo;
                String id;
                if (spec > 0) {
                    //从左向右滑
//                    Log.e(Tag, "0  spec=" + spec + ">>singleSpec=" + singleSpec);
                    mo = (int) (spec / singleSpec);
                    if (mo > pics.size())
                        mo = mo - pics.size();
                    mo = pics.size() + 1 - mo;
                    positive = false;
                } else {
                    //从右向左滑
//                    Log.e(Tag, "1  spec=" + spec + ">>singleSpec=" + singleSpec);
                    spec = 0 - spec;
                    mo = (int) (spec / singleSpec);
                    if (mo > pics.size())
                        mo = mo - pics.size();
                    positive = true;
                }
                if (mo != 0 && mo != 13) {
                    id = pics.get(mo - 1);
                    if (mo != NowID && !leftScreen) {
//                        Log.e(Tag, "ACTION_MOVE>>" + NowID);
                        NowID = mo;
                        Uri uri = Uri.parse(id);
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(uri)
                                .setControllerListener(new BaseControllerListener<ImageInfo>())
                                .build();
                        iv.setController(controller);
                    }
                }
                break;
        }
        return true;
    }

    //惯性
    private void intertia(int ID, final int time) {
        if (ID == 0)
            ID = pics.size();
        if (ID == pics.size() + 1)
            ID = 1;
//        Log.e(Tag, "intertia>>ID=" + ID + ">>time=" + time + ">>leftScreen=" + leftScreen);
        if (leftScreen) {
            if (ID < pics.size() + 1) {
                Uri uri = Uri.parse(pics.get(ID - 1));
                final int finalID = ID;
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .setControllerListener(new BaseControllerListener<ImageInfo>() {

                            @Override
                            public void onSubmit(String id, Object callerContext) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //惯性最后一定回到第一张图片
                                        if (time < intertiaEnd || finalID != 1) {
                                            if (positive)
                                                intertia(finalID + 1, time + intertiaOffset);
                                            else
                                                intertia(finalID - 1, time + intertiaOffset);
                                        }
                                    }
                                }, time);
//                                Log.e(Tag, "onSubmit>>" + finalID);
                                super.onSubmit(id, callerContext);
                            }
                        })
                        .build();
                iv.setController(controller);
            }
        }
    }

    //图片预加载图片，先将图片全部加载出来，解决滑动过程中白屏的问题
    private void setCache(final int ID) {
        if (ID < pics.size()) {
            Uri uri = Uri.parse(pics.get(ID));
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            setCache(ID + 1);
                        }
                    })
                    .build();
            iv.setController(controller);
        } else {
            Uri uri = Uri.parse(pics.get(ID - 1));
            iv.setImageURI(uri);
        }
    }

}
