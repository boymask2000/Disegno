package disegno.boymask.disegno;

public class Offset {
    private float position;

    public float getPosition() {
        return position;
    }

    public float getFatt() {
        return fatt;
    }

    private float fatt;

    public Offset(float pos, float f){
        this.position=pos;
        this.fatt=f;
    }
}
