package disegno.boymask.disegno;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by gposabella on 30/05/2016.
 */
public class SurfacePanel extends SurfaceView implements SurfaceHolder.Callback {
    private final Context context;
    private final MainActivity mainActivity;
    private MyThread mythread;
    private int screenWidth;
    private Paint mPaint = new Paint();


    private int tentativi;


    public SurfacePanel(Context ctx, AttributeSet attrSet, MainActivity mainActivity) {
        super(ctx, attrSet);
        context = ctx;
        this.mainActivity = mainActivity;
        screenWidth = mainActivity.getScreenWidth();
        //   getDims();
        // setClipBounds(new Rect(0, 0, screenWidth, screenWidth));
        getHolder().setFixedSize(screenWidth, screenWidth);
//setBackgroundColor(Color.RED);
        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        doDraw(canvas);
    }

    //***************************************************************************************
    void doDraw(Canvas canvas) {
        Point3D lista[] = new Point3D[]{
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 100),
                new Point3D(0, 100, 0),
                new Point3D(100, 0, 0),
                new Point3D(0, 100, 100),
                new Point3D(100, 0, 100),
                new Point3D(100, 100, 0)
        };
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(0, 0, 50);
        Point3D p3 = new Point3D(0, 50, 0);
        Point3D p4 = new Point3D(50, 0, 0);
        Point3D p5 = new Point3D(0, 50, 50);
        Point3D p6 = new Point3D(50, 0, 50);
        Point3D p7 = new Point3D(50, 50, 0);
        Point3D p8 = new Point3D(50, 50, 50);

        drawLine(canvas, p1, p2);
       drawLine(canvas, p1, p3);
        drawLine(canvas, p1, p4);
        drawLine(canvas, p2, p5);
        drawLine(canvas, p2, p6);

        drawLine(canvas, p7, p3);
        drawLine(canvas, p7, p4);
        drawLine(canvas, p7, p8);
        drawLine(canvas, p6, p8);
        drawLine(canvas, p5, p8);
        drawLine(canvas, p5, p3);
        drawLine(canvas, p4, p6);
    }

    private void drawLine(Canvas canvas, Point3D p1, Point3D p2) {
        Point2D pp1 = Converter.convert(p1);
        Point2D pp2 = Converter.convert(p2);
        mPaint.setStrokeWidth(4);
int fatt=1;
        canvas.drawLine(
                screenWidth/2 + fatt*pp1.getX(), //
                screenWidth/2 + fatt*pp1.getY(), //
                screenWidth/2 + fatt*pp2.getX(), //
                screenWidth/2 + fatt*pp2.getY(),//
                mPaint);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mythread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                mythread.join();

                retry = false;
            } catch (Exception e) {

                Log.v("Exception Occured", e.getMessage());
            }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

        Table tab = mainActivity.getTable();
        float x = event.getX();
        float y = event.getY();

        Pair cella = getCella(x, y);
        if (cella == null) return false;
        final TableCell cc = tab.getCell(cella.getX(), cella.getY());

        boolean ok = false;
        if (cella.getX() == 0) {
            ok = true;
            tab.rotateRow(cella.getY(), -1);
        }
        if (cella.getX() == tab.getSize() - 1) {
            ok = true;
            tab.rotateRow(cella.getY(), 1);
        }
        if (cella.getY() == 0) {
            ok = true;
            tab.rotateCol(cella.getX(), -1);
        }
        if (cella.getY() == tab.getSize() - 1) {
            ok = true;
            tab.rotateCol(cella.getX(), 1);
        }
        if (!ok) return false;

        cc.setBackgroundColor(Color.GREEN);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cc.setBackgroundColor(Color.WHITE);
            }
        }.start();

        tentativi++;
        mainActivity.setTentativi(tentativi);

        if (mainActivity.getTable().isRisolto())
            PopupMessage.info(mainActivity, "Completato !");

        return true;
    }


    private Pair getCella(float x, float y) {
        Table tab = mainActivity.getTable();
        int size = tab.getSize();
        int posx = (int) (x * size / screenWidth);
        int posy = (int) (y * size / screenWidth);

        if (posx < 0 || posx > size) return null;
        if (posy < 0 || posy > size) return null;


        return new Pair(posx, posy);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mythread = new MyThread(holder, context, this);

        mythread.setRunning(true);

        mythread.start();

    }

    public void update() {
        invalidate();
    }

    public void reset() {
        tentativi = 0;
    }


}
