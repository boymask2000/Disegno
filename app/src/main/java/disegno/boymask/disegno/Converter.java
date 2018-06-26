package disegno.boymask.disegno;

public class Converter {

    private static int distanza = 5;
    private static int altezza = 10;

    public static Point2D convert(Point3D p3) {
        float x = p3.getX();
        float y = p3.getY();
        float z = p3.getZ();

        float xp = x / (1 + y / distanza);
        float yp = (z * distanza + y * altezza) / (y + distanza);

        Point2D p = new Point2D(xp, yp);
        p.setId(p3.getId());
        return p;
    }
}
