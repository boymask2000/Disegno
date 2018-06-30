package disegno.boymask.disegno;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class Retta {
    private Point2D p1;
    private Point2D p2;



    private boolean draw=true;
    private static List<Retta> lista = new ArrayList<Retta>();


    public static Retta createRetta(Point2D p1, Point2D p2, boolean draw) {
        Retta r = new Retta(p1, p2, draw);
        lista.add(r);
        return r;
    }

    public static Retta createRettaHorizontal(Point2D p1, boolean draw) {
        Point2D p2 = new Point2D(p1.getX() + 1000, p1.getY());
        Retta r = new Retta(p1, p2, draw);
        lista.add(r);
        return r;
    }
    public static Retta createRettaVertical(Point2D p1, boolean draw) {
        Point2D p2 = new Point2D(p1.getX() , p1.getY()+1000);
        Retta r = new Retta(p1, p2, draw);
        lista.add(r);
        return r;
    }

    public static Retta createRetta(float x1, float y1, float x2, float y2) {
        Retta r = new Retta(x1, y1, x2, y2);
        lista.add(r);
        return r;
    }


    private Retta(Point2D p1, Point2D p2, boolean draw) {
        this.p1 = p1;
        this.p2 = p2;
        this.draw=draw;
    }

    private Retta(float x1, float y1, float x2, float y2) {
        this.p1 = new Point2D(x1, y1);
        this.p2 = new Point2D(x2, y2);
    }


    public Point2D getP1() {
        return p1;
    }

    public Point2D getP2() {
        return p2;
    }

    public boolean isVerticale() {
        return p1.getX() == p2.getX();
    }

    public boolean isOrizzontale() {
        return p1.getY() == p2.getY();
    }

    public double getCoffAngolare() {
        return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }

    public double getC() {
        double f = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        return -p1.getX() * f + p1.getY();
    }

    public double f(double x) {
        double f = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        return f * x - p1.getX() * f + p1.getY();
    }

    public Point2D intersect(Retta r) {
        if (isVerticale() && r.isVerticale()) return null;
        if (isOrizzontale() && r.isOrizzontale()) return null;

        if (isVerticale()) {
            float x = p1.getX();
            float y = (float) r.f(x);
            return new Point2D(x, y);
        }

        float x = (float) ((r.getC() - getC()) / (getCoffAngolare() - r.getCoffAngolare()));
        float y = (float) (r.getCoffAngolare() * x + r.getC());

        return new Point2D(x, y);

    }
    public boolean isDraw() {
        return draw;
    }
    public static void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setStrokeWidth(4);
        for (Retta r : lista)
            if(r.isDraw())
            canvas.drawLine(r.getP1().getX(), r.getP1().getY(), r.getP2().getX(), r.getP2().getY(), p);
    }
}
