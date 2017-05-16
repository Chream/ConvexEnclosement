import java.util.Random;
/**
 * Class NPunkter17 for aa finne n tilfeldige, ulike  punkter i x,y-planet
 * ver 7.mai 2015. Reworked to get not cohyll be a square for lage n
 ************************************************************************/
public class NPunkter17 {
    Random r;
    int n;
    byte [] bitArr;
    static int maxXY, xShift =3;
    int scaleFactor = 3;  // scaleFactor * scaleFactor * n= antall mulige punkter i planet (her: 3*n)
    final  int [] bitMask ={1,2,4,8,16,32,64,128};
    int xCentre = 0, yCentre = 0, maxVal;

    NPunkter17(int n) {
        this.n = n;
        maxXY = Math.max(10,(int) Math.sqrt(n) * scaleFactor); // største X og Y verdi
        while ((1<<xShift) < maxXY) xShift++;
        xShift = xShift - 3;                    // 8 bits per byte
        bitArr = new byte[(maxXY<<xShift |(maxXY>>3))  + 1];
        r = new Random(123);
        // added to get more 'roundish' set of points
        for (int i = 1 ; i <=20 ; i++){
            xCentre += r.nextInt(maxXY);
            yCentre += r.nextInt(maxXY);
        }
        xCentre /= 20;
        yCentre /= 20;
        maxVal = (int)(xCentre*1.33);
    }

    private void setUsed(int x, int y) {
        bitArr[(x<<xShift) | (y >>3)] |= bitMask[(y&7)];
    }

    private boolean used (int x, int y) {
        return  (bitArr[(x<<xShift) | (y >>3)] & bitMask[y&7]) != 0;
    }

    public void fyllArrayer(int [] x, int[] y) {
        int next =0;
        int xval, yval;
        // new 2017
        while (next < n) {
            do{
                xval = r.nextInt(maxXY)+1;
                yval = r.nextInt(maxXY)+1;
            } while (used (xval, yval) ||
                     Math.abs(xval-xCentre) + Math.abs(yval-yCentre) > maxVal);
            x[next] = xval;
            y[next] = yval;
            setUsed (xval,yval);
            next++;
        } // next point
    }// end fyllArrayer

    public IntList  lagIntList() {
        IntList res = new IntList(n);
        for (int i= 0; i <n; i++) res.add(i);
        return res;
    }// end fyllArrayer

} // end class NPunkter
