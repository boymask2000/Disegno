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
    Retta rette[] = new Retta[SIZE + 1];
    Retta r1;
    Retta r2;
    Retta baseLeft;
    Retta baseRight;
    Point2D basep1;
    Point2D basep2;
    Point2D centro;

    private int[][] table = new int[10][10];

    private void init() {
        table[2][0] = 1;
        table[2][1] = 1;
        table[2][2] = 1;
        table[2][3] = 1;
        table[2][4] = 1;
        table[2][5] = 1;
        table[2][6] = 1;
        table[2][7] = 1;
        table[2][8] = 1;
        table[2][9] = 1;

        table[1][5] = 1;
        table[1][2] = 1;
        table[3][4] = 1;

        table[1][8] = 1;
        table[3][8] = 1;
    }


    public SurfacePanel(Context ctx, AttributeSet attrSet, MainActivity mainActivity) {
        super(ctx, attrSet);
        init();
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

        centro = new Point2D(screenWidth / 2, screenWidth / 2);
        basep1 = new Point2D(10, screenWidth - 20);
        basep2 = new Point2D(screenWidth - 10, screenWidth - 20);

        baseLeft = Retta.createRetta(centro, basep1, true);
        baseRight = Retta.createRetta(centro, basep2, true);

        float baseSize = basep1.distanza(basep2);

        Point2D pLineTop = new Point2D(centro.getX(), centro.getY() + 50);
        Retta top = Retta.createRettaHorizontal(pLineTop, false);
        Point2D focusRight = new Point2D(screenWidth - 10, centro.getY());
        Point2D focusLeft = new Point2D(10, centro.getY());


        r1 = Retta.createRetta(basep2, focusLeft, false);
        r2 = Retta.createRetta(basep1, focusRight, false);


        for (int i = 0; i < SIZE; i++) {
            float x = basep1.getX() + i * baseSize / SIZE;
            Point2D pp = new Point2D(x, basep1.getY());
            Retta rr = Retta.createRetta(centro, pp, false);
            rette[i] = rr;
        }

        create(canvas, 2, 0, 0, 1);

/*
        createSvoltaSinistra(canvas, 2);
*/
        Retta.draw(canvas);

    }

    private void create(Canvas canvas, int x, int y, int dx, int dy) {
        if (dy > 0) {
            for (int i = y; i < 10; i++) {
                if (table[x - 1][i] == 1)
                    createSvoltaSinistra(canvas, i);
                if (table[x + 1][i] == 1)
                    createSvoltaDestra(canvas, i);
            }
        }
    }

    private void createSvoltaDestra(Canvas canvas, int metri) {
        Retta d = rette[metri];
        Point2D p = d.intersect(r1);
        Retta rr = Retta.createRettaHorizontal(p, false);
        Point2D q1 = rr.intersect(baseRight);

        Retta d2 = rette[metri + 1];
        Point2D p2 = d2.intersect(r1);
        Retta rr2 = Retta.createRettaHorizontal(p2, false);
        Point2D q2 = rr2.intersect(baseRight);

        int altezzaPorta = 2;

        Point2D pPorta = new Point2D(basep2.getX(), basep2.getY() - altezzaPorta * screenWidth / SIZE);
        Retta dir = Retta.createRetta(centro, pPorta, false);
        Retta q1Dir = Retta.createRettaVertical(q1, false);
        Retta q2Dir = Retta.createRettaVertical(q2, false);
        Point2D porta1 = q1Dir.intersect(dir);
        Point2D porta2 = q2Dir.intersect(dir);

        drawLine(canvas, porta1, q1);
        drawLine(canvas, porta2, q2);
        drawLine(canvas, porta1, porta2);
    }

    private void createSvoltaSinistra(Canvas canvas, int metri) {
        Retta d = rette[metri];
        Point2D p = d.intersect(r1);
        Retta rr = Retta.createRettaHorizontal(p, false);
        Point2D q1 = rr.intersect(baseLeft);

        Retta d2 = rette[metri + 1];
        Point2D p2 = d2.intersect(r1);
        Retta rr2 = Retta.createRettaHorizontal(p2, false);
        Point2D q2 = rr2.intersect(baseLeft);

        int altezzaPorta = 2;

        Point2D pPorta = new Point2D(basep1.getX(), basep2.getY() - altezzaPorta * screenWidth / SIZE);
        Retta dir = Retta.createRetta(centro, pPorta, false);
        Retta q1Dir = Retta.createRettaVertical(q1, false);
        Retta q2Dir = Retta.createRettaVertical(q2, false);
        Point2D porta1 = q1Dir.intersect(dir);
        Point2D porta2 = q2Dir.intersect(dir);

        drawLine(canvas, porta1, q1);
        drawLine(canvas, porta2, q2);
        drawLine(canvas, porta1, porta2);
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

    private void drawLine(Canvas canvas, Point2D pp1, Point2D pp2) {

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


}
