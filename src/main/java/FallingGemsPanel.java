
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.JPanel;

public class FallingGemsPanel extends JPanel implements MouseListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 200;
    // Offscreen kép
    private Image imageBuffer;
    // Annak grafikus buffere
    private Graphics graphicsBuffer;
    // Háttérkép
    private Image hatter;
    // Vesztettél kép
    private Image vesztettkep;
    // Kövek
    private Image[] kokepek = new Image[7];
    // Tábla
    int[][] tabla = new int[9][9];
    // Véletlenszám generátor
    private Random generator = new Random();
    // Pontszám
    int pont;
    // Kivalásztott-e már egy követ?
    boolean valasztott = false;
    // A kiválasztott kő indexe
    private int xindex, yindex;
    // Tábla helyzete a 0,0-hoz képest
    private int xoffset = 10;
    private int yoffset = 10;
    // Vesztett-e
    private boolean vesztett = false;
    // Mutatja, hogy éppen megy az egyik kő
    boolean menes = false;
    // Hibakeresés
    public static final boolean DEBUG = false;

    public FallingGemsPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // Háttér, vesztett kép és a kövek képeinek betöltése MediaTracker-rel
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(hatter, 0);
        tracker.addImage(vesztettkep, 1);
        for (int i = 0; i < 7; i++) {
            tracker.addImage(kokepek[i], i + 2);
        }
        try {
            tracker.waitForAll();
        } catch (Exception e) {
            System.err.println("Hiba a kepek betoltesekor: " + e);
        }

    }

    public void reset() {
        // Vesztett állapot false-ra állítása
        vesztett = false;
        // Pont nullázása
        pont = 0;
        // Tábla beállítása
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tabla[i][j] = 0;
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Offscreen technika
        imageBuffer = createImage(WIDTH, HEIGHT);
        graphicsBuffer = imageBuffer.getGraphics();
        // Első három kő leejtése
        ejtes();
        // MouseListener hozzaadasa
        addMouseListener(this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        //Ide kell rajzolni
        Graphics actg;
        actg = graphicsBuffer;
        actg.drawImage(hatter, 0, 0, this);
        if (!vesztett) {
            // Kövek kirajzolása
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (tabla[i][j] != 0) {
                        actg.drawImage(kokepek[tabla[i][j] - 1], xoffset + i * 20 + 3, yoffset + j * 20 + 3, this);
                    }
                }
            }
            // A választás jelölése
            if (valasztott) {
                actg.drawRect(xoffset + xindex * 20 + 16, yoffset + yindex * 20 + 16, 2, 2);
            }
        } else {
            actg.drawImage(vesztettkep, xoffset, yoffset, this);
        }
        // Pontszám
        actg.drawString("Pontszám: " + pont, 220, 180);

        // Frissítés
        g.drawImage(imageBuffer, 0, 0, this);
    }

    public boolean tele() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tabla[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ejtes() {
        for (int i = 0; i < 3; i++) {
            int x = generator.nextInt(9);
            int y = generator.nextInt(9);
            while (tabla[x][y] != 0) {
                x = generator.nextInt(9);
                y = generator.nextInt(9);
            }
            tabla[x][y] = generator.nextInt(7) + 1;
            if (tele()) {
                vesztett = true;
            }
        }
    }

    public int melyikSor(int x) {
        int melyik = (x - xoffset) / 20;
        if ((melyik >= 0) && (melyik < 9)) {
            return melyik;
        }
        return -1;
    }

    public int melyikOszlop(int y) {
        int melyik = (y - yoffset) / 20;
        if ((melyik >= 0) && (melyik < 9)) {
            return melyik;
        }
        return -1;
    }

    public int minszomszedpluszegy(int[][] tabla, int x, int y) {
        // Szomszédok közül a legkisebb kiválasztása, majd hozzáadunk egyet
        int[] ertekek = new int[4];
        int legkisebb = 100;
        ertekek[0] = (x > 0) && (tabla[x - 1][y] >= 0) ? tabla[x - 1][y] : 100;
        ertekek[1] = (x < 8) && (tabla[x + 1][y] >= 0) ? tabla[x + 1][y] : 100;
        ertekek[2] = (y > 0) && (tabla[x][y - 1] >= 0) ? tabla[x][y - 1] : 100;
        ertekek[3] = (y < 8) && (tabla[x][y + 1] >= 0) ? tabla[x][y + 1] : 100;
        if ((ertekek[0] <= ertekek[1]) && (ertekek[0] <= ertekek[2]) && (ertekek[0] <= ertekek[3])) {
            legkisebb = ertekek[0] + 1;
        }
        if ((ertekek[1] <= ertekek[0]) && (ertekek[1] <= ertekek[2]) && (ertekek[1] <= ertekek[3])) {
            legkisebb = ertekek[1] + 1;
        }
        if ((ertekek[2] <= ertekek[0]) && (ertekek[2] <= ertekek[1]) && (ertekek[2] <= ertekek[3])) {
            legkisebb = ertekek[2] + 1;
        }
        if ((ertekek[3] <= ertekek[0]) && (ertekek[3] <= ertekek[1]) && (ertekek[3] <= ertekek[2])) {
            legkisebb = ertekek[3] + 1;
        }
        if (legkisebb >= 100) {
            return -1;
        } else {
            return legkisebb;
        }
    }

    public void tablakiir(int[][] tabla) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tabla[j][i] >= 0) {
                    System.out.print("+");
                }
                System.out.print(tabla[j][i]);
            }
            System.out.println();
        }
        System.out.println("---");
    }

    public void mehet(int x1, int y1, int x2, int y2) {
        // Új tábla feltöltése: -2 - fal, -1 - üres
        int[][] megoldtabla = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                megoldtabla[i][j] = (tabla[i][j] == 0) ? -1 : -2;
            }
        }
        megoldtabla[x1][y1] = 0;
        megoldtabla[x2][y2] = -1;
        for (;;) {
            boolean valtoztatott = false;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
//                    if (debug) {
//                        tablakiir(megoldtabla);
//                    }
                    if (megoldtabla[i][j] == -1) {
                        megoldtabla[i][j] = minszomszedpluszegy(megoldtabla, i, j);
                        if (megoldtabla[i][j] != -1) {
                            valtoztatott = true;
                        }
                    }
                }
            }
            // Ha a célpont értéke megváltozott
            if (megoldtabla[x2][y2] != -1) {
                if (DEBUG) {
                    System.out.println("Ide lehet kovet mozgatni.");
                }
                // Menetelés útvonalának kiszámolása
                int[][] utvonal = new int[2][81];
                int actx = x2;
                int acty = y2;
                for (int i = megoldtabla[x2][y2]; i >= 0; i--) {
                    utvonal[0][i] = actx;
                    utvonal[1][i] = acty;
                    if ((actx > 0) && (megoldtabla[actx - 1][acty] == i - 1)) {
                        actx--;
                        continue;
                    }
                    if ((actx < 8) && (megoldtabla[actx + 1][acty] == i - 1)) {
                        actx++;
                        continue;
                    }
                    if ((acty > 0) && (megoldtabla[actx][acty - 1] == i - 1)) {
                        acty--;
                        continue;
                    }
                    if ((acty < 8) && (megoldtabla[actx][acty + 1] == i - 1)) {
                        acty++;
                        continue;
                    }
                }
                // Menetelő thread elindítása
                menes = true;
                Meno meno = new Meno(this, utvonal, megoldtabla[x2][y2]);
                meno.start();

                return;
            }

            // Ha egy változtatás sem történt a ciklus során
            if (!valtoztatott) {
                if (DEBUG) {
                    System.out.println("Ide nem lehet kovet mozgatni.");
                }
                return;
            }
        }
    }

    public int min(int x, int y) {
        if (x <= y) {
            return x;
        }
        return y;
    }

    public boolean otosben(int x, int y) {
        // Vízszintes keresés
        int startindex = (x - 4 >= 0) ? x - 4 : 0;
        int stopindex = (x + 4 <= 8) ? x + 4 : 8;
        int azonosszinu = 0;
        for (int i = startindex; i <= stopindex; i++) {
            if (tabla[i][y] == tabla[x][y]) {
                azonosszinu++;
            } else {
                azonosszinu = 0;
            }
            if (azonosszinu == 5) {
                return true;
            }
        }
        // Függőleges keresés
        startindex = (y - 4 >= 0) ? y - 4 : 0;
        stopindex = (y + 4 <= 8) ? y + 4 : 8;
        azonosszinu = 0;
        for (int i = startindex; i <= stopindex; i++) {
            if (tabla[x][i] == tabla[x][y]) {
                azonosszinu++;
            } else {
                azonosszinu = 0;
            }
            if (azonosszinu == 5) {
                return true;
            }
        }
        // Átlós keresés (bal föntről)
        azonosszinu = 0;
        for (int i = -4; i <= 4; i++) {
            if ((x + i >= 0) && (x + i <= 8) && (y + i >= 0) && (y + i <= 8) && (tabla[x + i][y + i] == tabla[x][y])) {
                azonosszinu++;
            }
            if ((x + i >= 0) && (x + i <= 8) && (y + i >= 0) && (y + i <= 8) && (tabla[x + i][y + i] != tabla[x][y])) {
                azonosszinu = 0;
            }
            if (azonosszinu == 5) {
                return true;
            }
        }
        // Átlós keresés (jobb föntről)
        azonosszinu = 0;
        for (int i = -4; i <= 4; i++) {
            if ((x - i >= 0) && (x - i <= 8) && (y + i >= 0) && (y + i <= 8) && (tabla[x - i][y + i] == tabla[x][y])) {
                azonosszinu++;
            }
            if ((x - i >= 0) && (x - i <= 8) && (y + i >= 0) && (y + i <= 8) && (tabla[x - i][y + i] != tabla[x][y])) {
                azonosszinu = 0;
            }
            if (azonosszinu == 5) {
                return true;
            }
        }

        return false;
    }

    public int sullyed() {
        int db = 0;
        int[][] ujtabla = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(tabla[i], 0, ujtabla[i], 0, 9);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if ((tabla[i][j] != 0) && (otosben(i, j))) {
                    ujtabla[i][j] = 0;
                    db++;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            System.arraycopy(ujtabla[i], 0, tabla[i], 0, 9);
        }
        return db;
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        if (vesztett) {
            reset();
            ejtes();
            repaint();
        }

        int x = evt.getX();
        int y = evt.getY();
        if ((vesztett) || (menes)) {
            return;
        }
        if (!valasztott) {
            xindex = melyikSor(x);
            yindex = melyikOszlop(y);
            if (DEBUG) {
                System.out.println("Kivalasztott cella: " + Integer.toString(xindex) + "," + Integer.toString(yindex));
            }
            if ((xindex != -1) && (yindex != -1) && (tabla[xindex][yindex] != 0)) {
                // Kijelölés
                if (DEBUG) {
                    System.out.println("Kivalasztva.");
                }
                valasztott = true;
                repaint();
            }
        } else {
            int newxindex = melyikSor(x);
            int newyindex = melyikOszlop(y);
            if ((newxindex == xindex) && (newyindex == yindex)) {
                // Kijelölés törlése
                if (DEBUG) {
                    System.out.println("Kivalasztas torolve.");
                }
                valasztott = false;
                repaint();
            } else {
                if (DEBUG) {
                    System.out.println("Kivalasztott cella: " + Integer.toString(newxindex) + "," + Integer.toString(newyindex));
                }
                if ((newxindex != -1) && (newyindex != -1) && (tabla[newxindex][newyindex] == 0)) {
                    if (DEBUG) {
                        System.out.println("Mozgas ellenorzese.");
                    }
                    mehet(xindex, yindex, newxindex, newyindex);
                }
            }
        }
        if (DEBUG) {
            System.out.println("A \"mouseDown\" esemeny vege.");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public void setHatter(Image hatter) {
        this.hatter = hatter;
    }

    public void setKokepek(Image[] kokepek) {
        this.kokepek = kokepek;
    }

    public void setVesztettkep(Image vesztettkep) {
        this.vesztettkep = vesztettkep;
    }
}
