package br.itarocha.cartanatal.core.decorator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public class DrawArc extends JComponent {

    @Override
    /*
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2.0f));
        g2.setPaint(Color.RED);
        g2.draw(new Arc2D.Double(50, 50, 300, 300, 0, 90, Arc2D.PIE));
        g2.setPaint(Color.GREEN);
        g2.draw(new Arc2D.Double(50, 50, 300, 300, 90, 90, Arc2D.PIE));
        g2.setPaint(Color.BLUE);
        g2.fill(new Arc2D.Double(50, 50, 300, 300, 180, 90, Arc2D.PIE));
    }
    */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GeneralPath path=new GeneralPath();

        //d="M 405.344799471418, 706.2033566416449 l 0.7635427816311477, 43.743336663092116 A 350, 350,0 ,0, 0, 580.2633262185191, 700.0085552457392 l -22.53291577731488, -37.50106940571741 A 306.25, 306.25,0 ,0, 1, 405.344799471418, 706.2033566416449"

        path.moveTo(405.344799471418, 706.2033566416449 );
        path.lineTo(0.7635427816311477, 43.743336663092116);
        path.curveTo(350, 350,0 , 0, 580.2633262185191, 700.0085552457392 );
        path.lineTo(-22.53291577731488, -37.50106940571741);
        path.curveTo(306.25, 306.25,0, 1, 405.344799471418, 706.2033566416449);


        path.curveTo(399.48516,366.83843 ,398.54916,368.02743 ,397.41516,372.27043 );
        path.curveTo(394.75116,382.25643 ,392.96616,392.69543 ,391.09516,402.03043 );
        path.curveTo(390.35916,405.62343 ,389.79116,406.92443 ,392.62616,409.52743 );
        path.curveTo(406.00316,421.83343 ,442.19716,458.07143 ,444.89016,482.76843 );
        path.curveTo(431.76716,528.31343 ,393.39116,574.56743 ,350.22516,594.56743 );
        path.curveTo(316.63916,610.12643 ,278.88716,614.34043 ,242.18316,610.35243 );
        path.curveTo(232.12112,609.27843 ,228.38012,619.29143 ,238.47016,621.92243 );
        path.curveTo(274.01216,631.28543 ,320.32416,637.73643 ,356.57416,628.91043 );
        path.curveTo(420.03416,613.46343 ,456.48216,533.71643 ,457.61616,470.82943);

        path.closePath();
        g2d.draw(path);
        g2d.fill(path);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Arc Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DrawArc());
        frame.pack();
        frame.setSize(new Dimension(800, 800));
        frame.setVisible(true);
    }
}
/*
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GeneralPath path=new GeneralPath();

        path.moveTo(457.61616,470.82943 );
        path.curveTo(458.41016,425.70843 ,427.74316,392.55343 ,403.93516,370.91243 );
        path.curveTo(399.48516,366.83843 ,398.54916,368.02743 ,397.41516,372.27043 );
        path.curveTo(394.75116,382.25643 ,392.96616,392.69543 ,391.09516,402.03043 );
        path.curveTo(390.35916,405.62343 ,389.79116,406.92443 ,392.62616,409.52743 );
        path.curveTo(406.00316,421.83343 ,442.19716,458.07143 ,444.89016,482.76843 );
        path.curveTo(431.76716,528.31343 ,393.39116,574.56743 ,350.22516,594.56743 );
        path.curveTo(316.63916,610.12643 ,278.88716,614.34043 ,242.18316,610.35243 );
        path.curveTo(232.12112,609.27843 ,228.38012,619.29143 ,238.47016,621.92243 );
        path.curveTo(274.01216,631.28543 ,320.32416,637.73643 ,356.57416,628.91043 );
        path.curveTo(420.03416,613.46343 ,456.48216,533.71643 ,457.61616,470.82943);

        path.closePath();
        g2d.draw(path);
        g2d.fill(path);
    }
}

 */