package disegno.boymask.disegno;

import android.graphics.Canvas;

public class Point3D {



    private final int id;
    private float x;
    private float y;
    private float z;



    public Point3D(int id,float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
     this.id=id;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
    public int getId() {
        return id;
    }

}
