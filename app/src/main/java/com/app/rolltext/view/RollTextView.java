package com.app.rolltext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 王立强 on 2016/11/3.
 */

public class RollTextView extends TextView {
    //高位快
    public static final int HIGH_FAST = 0;
    //高位慢
    public static final int HIGH_SLOW = 1;
    //速度相同
    public static final int ALL = 2;
    //滚动总行数
    private int maxLine = 10;
    //当前字符长度
    private int numLength = 0;
    //当前text
    private String text;
    //滚动速度数组
    private int[] deviationList;
    //总滚动距离数组
    private int[] deviationSum;
    //滚动完成判断
    private int[] overLine;
    private Paint p;
    //第一次绘制
    private boolean firstIn = true;
    //滚动中
    private boolean auto = true;
    //Test int值类型
    private ArrayList<Integer> arrayListText;
    //字体宽度
    private float f0;
    //基准线
    private int baseLine;

    public RollTextView(Context context) {
        super(context);
    }

    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RollTextView setText(String text) {
        super.setText(text);
        return this;
    }

    //按系统提供的类型滚动
    public RollTextView setDeviation(int deviationTpye) {
        this.text = getText().toString();

        deviationSum = new int[text.length()];
        overLine = new int[text.length()];
        deviationList = new int[text.length()];
        switch (deviationTpye) {
            case HIGH_FAST:
                for (int i = 0; i < text.length(); i++) {
                    deviationList[i] = 20 - i;
                }
                break;
            case HIGH_SLOW:
                for (int i = 0; i < text.length(); i++) {
                    deviationList[i] = 10 + i;
                }
                break;
            case ALL:
                for (int i = 0; i < text.length(); i++) {
                    deviationList[i] = 20;
                }
                break;
        }
        return this;
    }

    //自定义滚动速度数组
    public RollTextView setDeviation(int[] list) {
        this.text = getText().toString();

        deviationSum = new int[list.length];
        overLine = new int[list.length];
        deviationList = list;
        return this;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (firstIn) {
            firstIn = false;
            super.onDraw(canvas);
            p = getPaint();
            Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
            baseLine = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            float[] widths = new float[4];
            p.getTextWidths("9999", widths);
            f0 = widths[0];
            invalidate();
        }
        drawNumber(canvas);
    }

    //绘制
    private void drawNumber(Canvas canvas) {
        for (int j = 0; j < numLength; j++) {
            for (int i = 1; i < maxLine; i++) {
                if (i == maxLine - 1 && i * baseLine + deviationSum[j] <= baseLine) {
                    deviationList[j] = 0;
                    overLine[j] = 1;
                    int auto = 0;
                    for (int k = 0; k < numLength; k++) {
                        auto += overLine[k];
                    }
                    if (auto == numLength * 2 - 1) {
                        this.auto = false;
                        handler.removeCallbacks(task);
                        invalidate();
                    }
                }
                if (overLine[j] == 0) {
                    canvas.drawText(setBack(arrayListText.get(j), maxLine - i - 1) + "",
                            0 + f0 * j,
                            i * baseLine + deviationSum[j],
                            p);
                } else {
                    //定位后画一次就好啦
                    if (overLine[j] == 1) {
                        overLine[j]++;
                        canvas.drawText(arrayListText.get(j) + "",
                                0 + f0 * j,
                                baseLine,
                                p);
                    }
                }
            }
        }
    }

    //设置上方数字0-9递减
    private int setBack(int c, int back) {
        if (back == 0) return c;
        back = back % 10;
        int re = c - back;
        if (re < 0) re = re + 10;
        return re;
    }

    //开始滚动
    public void start() {
        this.text = getText().toString();

        numLength = text.length();
        arrayListText = getList(text);
        handler.postDelayed(task, 17);
        auto = true;
    }

    public RollTextView setMaxLine(int l) {
        this.maxLine = l;
        return this;
    }

    private ArrayList<Integer> getList(String s) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < s.length(); i++) {
            String ss = s.substring(i, i + 1);
            int a = Integer.parseInt(ss);
            arrayList.add(a);
        }
        return arrayList;
    }

    private static final Handler handler = new Handler();

    private final Runnable task = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            if (auto) {
                handler.postDelayed(this, 20);
                for (int j = 0; j < numLength; j++) {
                    deviationSum[j] -= deviationList[j];

                }
                invalidate();
            }

        }
    };
}
