package br.itarocha.cartanatal.core.decorator;

public class NewDesenho {

    private static final int SHIFT_IN_DEGREES = 180;

    public static void main(String[] args) {
        drawUniverse();
    }


    private static void drawUniverse(){
        //var universe = this.universe;
        //var wrapper = astrology.utils.getEmptyWrapper( universe, astrology._paperElementId + "-" + astrology.ID_RADIX + "-" + astrology.ID_SIGNS);

        //console.log("shift = "+this.shift);

        String[] COLORS_SIGNS = {   "#F07778","#F39C57","#FCC859","#FCE558","#FFFF82","#D3FF82",
                                    "#65FF65","#64FDB1","#7ED2F8","#A0A0FD","#D9B9FD","#F8B5FE"};

        // colors
        int width = 800;
        int height = 800;
        int MARGIN = 50;

        double cx = width / 2;
        double cy = height / 2;
        double radius = height/2 - MARGIN;
        int shift = 91;
        int INNER_CIRCLE_RADIUS_RATIO = 8;
        for ( int i = 0, step = 30, start = shift, len = COLORS_SIGNS.length; i < len; i++ ){

            double thickness = radius-(radius/INNER_CIRCLE_RADIUS_RATIO);

            String segment = segment( cx, cy, radius, start, start+step, thickness);
            //segment.setAttribute("fill", astrology.STROKE_ONLY ? "none" : astrology.COLORS_SIGNS[i]);
            //segment.setAttribute("id", astrology._paperElementId + "-" + astrology.ID_RADIX + "-" + astrology.ID_SIGNS + "-" + i)
            //segment.setAttribute("stroke", astrology.STROKE_ONLY ? astrology.CIRCLE_COLOR: "none");
            //segment.setAttribute("stroke-width", astrology.STROKE_ONLY ? 1 : 0);

            //console.log(segment);
            System.out.println(segment);

            //wrapper.appendChild( segment );

            start += step;
        };

        // signs
        /*
        for( var i = 0, step = 30, start = 15 + this.shift, len = astrology.SYMBOL_SIGNS.length; i < len; i++ ){
            var position = astrology.utils.getPointPosition( this.cx, this.cy, this.radius - (this.radius/astrology.INNER_CIRCLE_RADIUS_RATIO)/2, start);
            wrapper.appendChild( this.paper.getSymbol( astrology.SYMBOL_SIGNS[i], position.x, position.y));
            start += step;
        }
         */
    };

    private static String segment( double x, double y, double radius, double a1, double a2, double thickness /*, lFlag, sFlag*/ ){

        // @see SVG Path arc: https://www.w3.org/TR/SVG/paths.html#PathData
        //var LARGE_ARC_FLAG = lFlag || 0;
        //var SWEET_FLAG = sFlag || 0;

        int LARGE_ARC_FLAG = 0;
        int SWEET_FLAG = 0;



        a1 = ((SHIFT_IN_DEGREES - a1) % 360) * Math.PI / 180;
        a2 = ((SHIFT_IN_DEGREES - a2 ) % 360) * Math.PI / 180;

        StringBuilder sb = new StringBuilder();

        //d="M 405.344799471418, 706.2033566416449 l 0.7635427816311477, 43.743336663092116 A 350, 350,0 ,0, 0, 580.2633262185191, 700.0085552457392 l -22.53291577731488, -37.50106940571741 A 306.25, 306.25,0 ,0, 1, 405.344799471418, 706.2033566416449"


        sb.append("<path d=\"");

        double startX = (x + thickness * Math.cos(a1));
        double startY = (y + thickness * Math.sin(a1));

        // A 7 parâmetros
        //(rx, ry, x-axis-rotation, large-arc-flag, sweep-flag, x, y)

        sb.append(
        "M " + startX + ", " + startY +
        " l " + ((radius-thickness) * Math.cos(a1)) + ", "+ ((radius-thickness) * Math.sin(a1)) +
        " A " + radius + ", " + radius + ", 0 ," +  LARGE_ARC_FLAG + ", " + SWEET_FLAG + ", " + ( x + radius * Math.cos(a2) ) + ", " + ( y + radius * Math.sin(a2) ) +
        " l " + ( (radius-thickness)  * -Math.cos(a2) ) + ", " + ( (radius-thickness) * -Math.sin(a2) ) +
        " A " + thickness + ", " + thickness + ",0 ," +  LARGE_ARC_FLAG + ", " + 1 + ", " + startX + ", " + startY +
        "\"");

        /*

        Path2D p = new Path2D.Double();
        p.moveTo(0,5);
        p.lineTo(2,-3);
        p.lineTo(-4,1);
        p.close();

         */


        //segment.setAttribute("fill", "none");
        return sb.toString();
    };



    /*

var data = {
				"planets":{
					"Sun":[238],
					"Moon":[240],
					"Mercury":[221],
					"Venus":[261],
					"Mars":[208],
					"Jupiter":[20],
					"Saturn":[260],
					"Uranus":[265],
					"Neptune":[276],
					"Pluto":[220]
				},
				"cusps":[269, 293, 320, 349, 23, 57, 89, 113, 140, 169, 203, 237]
			};

			var settings = {COLOR_ARIES : "#F07778",
                          COLOR_TAURUS : "#F39C57",
                          COLOR_GEMINI: "#FCC859",
                          COLOR_CANCER : "#FCE558",
                          COLOR_LEO : "#FFFF82",
                          COLOR_VIRGO : "#D3FF82",
                          COLOR_LIBRA : "#65FF65",
                          COLOR_SCORPIO : "#64FDB1",
                          COLOR_SAGITTARIUS : "#7ED2F8",
                          COLOR_CAPRICORN : "#A0A0FD",
                          COLOR_AQUARIUS : "#D9B9FD",
                          COLOR_PISCES : "#F8B5FE",


     */


    /*
    astrology.Radix.prototype.drawUniverse = function(){
        var universe = this.universe;
        var wrapper = astrology.utils.getEmptyWrapper( universe, astrology._paperElementId + "-" + astrology.ID_RADIX + "-" + astrology.ID_SIGNS);

        //console.log("shift = "+this.shift);

        // colors
        for( var i = 0, step = 30, start = this.shift, len = astrology.COLORS_SIGNS.length; i < len; i++ ){

            var segment = this.paper.segment( this.cx, this.cy, this.radius, start, start+step, this.radius-this.radius/astrology.INNER_CIRCLE_RADIUS_RATIO);
            segment.setAttribute("fill", astrology.STROKE_ONLY ? "none" : astrology.COLORS_SIGNS[i]);
            segment.setAttribute("id", astrology._paperElementId + "-" + astrology.ID_RADIX + "-" + astrology.ID_SIGNS + "-" + i)
            segment.setAttribute("stroke", astrology.STROKE_ONLY ? astrology.CIRCLE_COLOR: "none");
            segment.setAttribute("stroke-width", astrology.STROKE_ONLY ? 1 : 0);

            console.log(segment);

            wrapper.appendChild( segment );

            start += step;
        };

        // signs
        for( var i = 0, step = 30, start = 15 + this.shift, len = astrology.SYMBOL_SIGNS.length; i < len; i++ ){
            var position = astrology.utils.getPointPosition( this.cx, this.cy, this.radius - (this.radius/astrology.INNER_CIRCLE_RADIUS_RATIO)/2, start);
            wrapper.appendChild( this.paper.getSymbol( astrology.SYMBOL_SIGNS[i], position.x, position.y));
            start += step;
        }
    };
*/


/*
	astrology.SVG.prototype.segment = function segment( x, y, radius, a1, a2, thickness, lFlag, sFlag){

	 	// @see SVG Path arc: https://www.w3.org/TR/SVG/paths.html#PathData
	 	var LARGE_ARC_FLAG = lFlag || 0;
	 	var SWEET_FLAG = sFlag || 0;

        a1 = ((astrology.SHIFT_IN_DEGREES - a1) % 360) * Math.PI / 180;
        a2 = ((astrology.SHIFT_IN_DEGREES - a2 ) % 360) * Math.PI / 180;

		var segment = document.createElementNS( context.root.namespaceURI, "path");
		segment.setAttribute("d", "M " + (x + thickness * Math.cos(a1)) + ", " + (y + thickness * Math.sin(a1)) + " l " + ((radius-thickness) * Math.cos(a1)) + ", " + ((radius-thickness) * Math.sin(a1)) + " A " + radius + ", " + radius + ",0 ," +  LARGE_ARC_FLAG + ", " + SWEET_FLAG + ", " + ( x + radius * Math.cos(a2) ) + ", " + ( y + radius * Math.sin(a2) ) + " l " + ( (radius-thickness)  * -Math.cos(a2) ) + ", " + ( (radius-thickness) * -Math.sin(a2) ) + " A " + thickness + ", " + thickness + ",0 ," +  LARGE_ARC_FLAG + ", " + 1 + ", " + ( x + thickness * Math.cos(a1) ) + ", " + ( y + thickness * Math.sin(a1)));
		segment.setAttribute("fill", "none");
		return segment;
	};

 */

}


/*

public class DrawArc extends JComponent {
    @Override
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Arc Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DrawArc());
        frame.pack();
        frame.setSize(new Dimension(420, 450));
        frame.setVisible(true);
    }
}

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