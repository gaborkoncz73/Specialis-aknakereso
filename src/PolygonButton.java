import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A mező egy gombjának tulajdonságaiért felelős osztály, amely felülírja a JButton osztályt.
 */
class PolygonButton extends JButton {
    private transient Shape shape;
    private final int vertices;
    private final int rectangleX;
    private final int rectangleY;
    private final double  rotate;
    private boolean processed;
    private boolean bomb;
    private boolean special;
    private boolean usingSpecial;
    private boolean flagged;

    /**
     * A polygonButton osztály konstruktora
     *
     * @param vertices int
     * @param rectangleX int
     * @param rectangleY int
     * @param rotate int
     * @param listener MouseListener
     * @param font int
     */
    public PolygonButton(int vertices, int rectangleX, int rectangleY, double rotate, MouseListener listener, int font) {
        this.rotate = rotate;
        this.vertices = vertices;
        this.rectangleX = rectangleX;
        this.rectangleY = rectangleY;
        this.special = false;
        this.flagged = false;
        this.bomb = false;
        this.processed = false;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Arial", Font.PLAIN, font));
        updateShape();
        addMouseListener(listener);
    }

    /**
     * Módosítja a JButton alakját.
     */
    private void updateShape() {
        int[] xPoints = new int[vertices];
        int[] yPoints = new int[vertices];
        int radius = Math.min(rectangleX,rectangleY)/2;
        for (int i = 0; i < vertices; i++) {
            xPoints[i] = (int) ((double)(rectangleX)/2 + radius * Math.cos(2 * Math.PI * i / vertices + rotate));
            yPoints[i] = (int) ((double)(rectangleX)/2 + radius * Math.sin(2*Math.PI * i / vertices+ rotate));
        }
        shape = new Polygon(xPoints, yPoints, vertices);
    }

    /**
     * Megnézi, hogy az kattintás a módosított shapen belülre esik-e.
     *
     * @param x   the <i>x</i> coordinate of the point
     * @param y   the <i>y</i> coordinate of the point
     * @return boolean
     */
    @Override
    public boolean contains(int x, int y) {
        return shape.contains(x, y);
    }

    /**
     * Felülírja a JButton paintComponent metódusát, amely a módosított shapet fogja kirajzolni.
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (getModel().isArmed()) {
            g2.setColor(Color.lightGray);
        } else {
            g2.setColor(getBackground());
        }
        g2.fill(shape);
        g2.setColor(Color.BLACK);
        g2.draw(shape);
        g2.dispose();
        super.paintComponent(g);
    }

    /**
     * Beállítja az adott gombot enabled vagy nem enabled-re és ha nem enabled-re állítja, akkor még a hátterét is átszínezi.
     *
     * @param enabled  true to enable the button, otherwise false
     */
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(!enabled){
            setBackground(Color.cyan);
        }
    }

    /**
     * A paraméterül kapott gombot a paraméterül kapott koordinátáknak és a paraméterül kapott nagyságnak megfelelő helyre teszi.
     *
     * @param button PolygonButton
     * @param x int
     * @param y int
     * @param rectangleX int
     * @param rectangleY int
     */
    protected void setButton(PolygonButton button,int x, int y, int rectangleX, int rectangleY){
        button.setBackground(Color.WHITE);
        button.setBounds(x, y, rectangleX, rectangleY);
    }

    /**
     * Visszaadja, hog az adott mező tartalmaz-e bombát.
     *
     * @return boolean
     */
    public boolean hasBomb(){
        return bomb;
    }

    /**
     * Beállítja, hogy az adott mező tartalmazzon-e bombát vagy sem.
     *
     * @param bomb boolean
     */
    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    /**
     * Megnézi, hogy az adott mező felvane-e már dolgozva.
     *
     * @return boolean
     */
    public boolean isProcessed(){
        return processed;
    }

    /**
     * Beállítja, hogy az adott mező fel legyen-e dolgozva vagy sem.
     *
     * @param processed boolean
     */
    public void setProcessed(boolean processed){
        this.processed = processed;
    }

    /**
     * Visszaadja, hogy az adott mező meg vane-e jelölve.
     *
     * @return boolean
     */
    public boolean getFlagged(){
        return flagged;
    }

    /**
     * Beállítja az adott mezőt jelöltre vagy nem jelöltre plusz a hátterét.
     *
     * @param flag boolean
     */
    public void setFlagged(boolean flag){
        if(flag){
            this.setBackground(Color.red);
            flagged = true;
        }else{
            this.setBackground(Color.white);
            flagged = false;
        }
    }

    /**
     * Beállítja a speciális mezőt
     *
     * @param special boolean
     */
    public void setSpecial(boolean special){
        this.special = special;
    }

    /**
     * Visszaadja, hogy az adott mező sepciális-e.
     *
     * @return boolean
     */
    public boolean getSpecial(){
        return this.special;
    }

    /**
     * Visszaadja, hogy éppen speciális segítség használatban van-e.
     *
     * @return boolean
     */
    public boolean getUsingSpecial(){
        return usingSpecial;
    }

    /**
     * Beállítja, hogy éppen használatban van-e a speciális segítség vagy sem.
     *
     * @param uP boolean
     */
    public void setUsingSpecial(boolean uP){
        usingSpecial = uP;
    }
}