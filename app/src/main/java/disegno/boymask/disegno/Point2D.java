package disegno.boymask.disegno;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Point2D {
    private float x;
    private float y;


    private int id;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void draw(Offset off, Canvas canvas) {
        canvas.drawText("" + id, off.getPosition() + off.getFatt() * x, off.getPosition() + off.getFatt() * y, new Paint());
    }
    public float distanza(Point2D p){
        return (float) Math.sqrt((x-p.getX())*(x-p.getX())+(y-p.getY())*(y-p.getY()));
    }
}
