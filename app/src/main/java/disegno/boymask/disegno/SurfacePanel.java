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
    private static final int SIZE = 10;
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

    Retta rette[] = new Retta[SIZE + 1];
    Retta r1;
    Retta r2;
    Retta baseLeft;
    Retta baseRight;
    Point2D p1;
    Point2D p2 ;Point2D centro;
    //***************************************************************************************
    void doDraw(Canvas canvas) {

         centro = new Point2D(screenWidth / 2, screenWidth / 2);
         p1 = new Point2D(10, screenWidth - 20);
         p2 = new Point2D(screenWidth - 10, screenWidth - 20);

        baseLeft = Retta.createRetta(centro, p1);
        baseRight = Retta.createRetta(centro, p2);

        float baseSize = p1.distanza(p2);

        Point2D pLineTop = new Point2D(centro.getX(), centro.getY() + 50);
        Retta top = Retta.createRettaHorizontal(pLineTop);
        Point2D focusRight = new Point2D(screenWidth - 10, centro.getY());
        Point2D focusLeft = new Point2D(10, centro.getY());


        r1 = Retta.createRetta(p2, focusLeft);
        r2 = Retta.createRetta(p1, focusRight);


        for (int i = 0; i < SIZE; i++) {
            float x = p1.getX() + i * baseSize / SIZE;
            Point2D pp = new Point2D(x, p1.getY());
            Retta rr = Retta.createRetta(centro, pp);
            rette[i] = rr;
        }

        createSvoltaDestra(canvas,2);

        Retta.draw(canvas);
    }

    private void createSvoltaDestra(Canvas canvas,int metri) {
        Retta d = rette[metri];
        Point2D p = d.intersect(r2);
        Retta rr = Retta.createRettaHorizontal(p);
        Point2D q1 = rr.intersect(baseRight);

        Retta d2 = rette[metri + 1];
        Point2D p2 = d2.intersect(r2);
        Retta rr2 = Retta.createRettaHorizontal(p2);
        Point2D q2 = rr2.intersect(baseRight);

        int altezzaPorta=3;

        Point2D pPorta=new Point2D(p2.getX(), altezzaPorta*SIZE);
        Retta dir=Retta.createRetta(centro, pPorta);
        Retta rporta = Retta.createRettaVertical(pPorta);
        Point2D porta1=dir.intersect(rporta);
        Point2D porta2=r2.intersect(rporta);

        drawLine(canvas,porta1,porta2 );
    }

    void doDraw1(Canvas canvas) {
        float x0 = -30;
        float y0 = -30;
        float z0 = 10;

        Offset offset = new Offset(screenWidth / 4, 50);

        Point3D p1 = new Point3D(1, x0, y0, z0);
        Point3D p2 = new Point3D(2, x0, y0, z0 + 50);
        Point3D p3 = new Point3D(3, x0, y0 + 50, z0);
        Point3D p4 = new Point3D(4, x0 + 50, y0, z0);
        Point3D p5 = new Point3D(5, x0, y0 + 50, z0 + 50);
        Point3D p6 = new Point3D(6, x0 + 50, y0, z0 + 50);
        Point3D p7 = new Point3D(7, x0 + 50, y0 + 50, z0);
        Point3D p8 = new Point3D(8, x0 + 50, y0 + 50, z0 + 50);

        drawLine(offset, canvas, p1, p2);
        drawLine(offset, canvas, p1, p3);
        drawLine(offset, canvas, p1, p4);
        drawLine(offset, canvas, p2, p5);
        drawLine(offset, canvas, p2, p6);

        drawLine(offset, canvas, p7, p3);
        drawLine(offset, canvas, p7, p4);
        //  drawLine(offset, canvas, p7, p8);
        drawLine(offset, canvas, p6, p8);
        drawLine(offset, canvas, p5, p8);
        drawLine(offset, canvas, p5, p3);
        drawLine(offset, canvas, p4, p6);
    }

    private void drawLine(Offset off, Canvas canvas, Point3D p1, Point3D p2) {
        Point2D pp1 = Converter.convert(p1);
        Point2D pp2 = Converter.convert(p2);
        mPaint.setStrokeWidth(4);

        pp1.draw(off, canvas);
        pp2.draw(off, canvas);

        canvas.drawLine(
                off.getPosition() + off.getFatt() * pp1.getX(), //
                off.getPosition() + off.getFatt() * pp1.getY(), //
                off.getPosition() + off.getFatt() * pp2.getX(), //
                off.getPosition() + off.getFatt() * pp2.getY(),//
                mPaint);
    }
    private void drawLine(Canvas canvas,Point2D pp1,  Point2D pp2 ) {

        mPaint.setStrokeWidth(4);



        canvas.drawLine(
                 pp1.getX(), //
                 pp1.getY(), //
                 pp2.getX(), //
                 pp2.getY(),//
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
