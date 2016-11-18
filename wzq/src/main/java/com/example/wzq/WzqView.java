package com.example.wzq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class WzqView extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE=10;

    private Paint mPaint=new Paint();

    private Bitmap mWhetePiece,mBlackPiece;

    private float ratioPieceOfLineHeight=3*1.0f/4;

    private boolean mIsWhite=true;//白棋先手
    private List<Point> mWhiteArray=new ArrayList<>();
    private List<Point> mBlackArray=new ArrayList<>();


    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;

    private int MAX_COUNT_IN_LINE=5;

    public WzqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置背景颜色
        setBackgroundColor(0x44ff0000);
        //调用方法init
        init();

    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhetePiece= BitmapFactory.decodeResource(getResources(),R.drawable.baizi);
        mBlackPiece=BitmapFactory.decodeResource(getResources(),R.drawable.heizi);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=Math.min(widthSize,heightSize);

        if (widthMode==MeasureSpec.UNSPECIFIED){
            width=heightSize;
        }else if (heightMode==MeasureSpec.UNSPECIFIED){
            width=widthSize;
        }
         setMeasuredDimension(width,width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth=w;
        mLineHeight=mPanelWidth*1.0f/MAX_LINE;

        int pieceWidth= (int) (mLineHeight*ratioPieceOfLineHeight);

        mWhetePiece=Bitmap.createScaledBitmap(mWhetePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) return false;

        int action=event.getAction();
        if (action==MotionEvent.ACTION_UP){
            int x= (int) event.getX();
            int y= (int) event.getY();

            Point p=getValidPoint( x, y);

            if (mWhiteArray.contains(p)||mBlackArray.contains(p))
            {
            return false;
            }

            if (mIsWhite)
            {
                mWhiteArray.add(p);
            }else
            {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite=!mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x/mLineHeight),(int) (y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);

        checkGameOver();
    }

    private void checkGameOver()
    {
        boolean whiteWin=chedkFiveInLine(mWhiteArray);
        boolean blackWin=chedkFiveInLine(mBlackArray);

        if (whiteWin||blackWin)
        {
            mIsGameOver=true;
            mIsWhiteWinner=whiteWin;

            String text=mIsWhiteWinner?"白棋胜利":"黑棋胜利";

            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean chedkFiveInLine(List<Point> points) {
        for (Point p : points)
        {
            int x=p.x;
            int y=p.y;

            boolean win=checkHorizontal(x,y,points);
            if (win)return true;

            win=checkVertical(x,y,points);
            if (win)return true;

            win=checkzuoxie(x,y,points);
            if (win)return true;

            win=checkyouxie(x,y,points);
            if (win)return true;

        }

        return false;
    }
/**
 * 判断 x , y 位置的棋子，是否横向有相邻的五个一致
 * */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        //横向左
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x-i,y)))
            {
                count++;
            }else
            {
                break;
            }
        }
        //横向右
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x+i,y)))
            {
                count++;
            }else
            {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)return true;


        return false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        //下
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x,y+i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        //上
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x,y-i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)return true;


        return false;
    }
    private boolean checkzuoxie(int x, int y, List<Point> points) {
        int count=1;
        //左斜
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x-i,y+i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(y+i,y-i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        //右斜
        if (count==MAX_COUNT_IN_LINE)return true;


        return false;
    }
    private boolean checkyouxie(int x, int y, List<Point> points) {
        int count=1;
        //
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x+i,y+i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        for (int i=1;i<MAX_COUNT_IN_LINE;i++)
        {
            if (points.contains(new Point(x-i,y-i)))
            {
                count++;
            }else
            {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)return true;


        return false;
    }

    //绘制棋子
    private void drawPieces(Canvas canvas) {
        for (int i=0,n =mWhiteArray.size();i<n;i++)
        {
            Point whitePoint=mWhiteArray.get(i);
            canvas.drawBitmap(mWhetePiece,
                            (whitePoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                            (whitePoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);

        }
        for (int i=0,n =mBlackArray.size();i<n;i++)
        {
            Point blackPoint=mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null);

        }
    }
    //绘制棋盘
    private void drawBoard(Canvas canvas) {
        int w=mPanelWidth;
        float lineHeight=mLineHeight;

        for(int i=0;i<MAX_LINE;i++){
            int startX= (int) (lineHeight/2);
            int endX= (int) (w-lineHeight/2);

            int y= (int) ((0.5+i)*lineHeight);

            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();

        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, (ArrayList<? extends Parcelable>) mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, (ArrayList<? extends Parcelable>) mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
    public void reStart(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;

        invalidate();
    }

}
