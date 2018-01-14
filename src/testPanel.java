


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class testPanel extends JPanel {
   private int x1, y1, x2, y2;

   public testPanel()
   {
      addMouseListener(
         new MouseAdapter() {
            public void mousePressed( MouseEvent e )
            {
               x1 = e.getX();
               y1 = e.getY();
            }

            public void mouseReleased( MouseEvent e )
            {
               x2 = e.getX();
               y2 = e.getY();
               repaint();
            }
         }
      );

      addMouseMotionListener(
         new MouseMotionAdapter() {
            public void mouseDragged( MouseEvent e )
            {
               x2 = e.getX();
               y2 = e.getY();
               repaint();
            }
         }
      );
   }

   public Dimension getPreferredSize()
   {
      return new Dimension( 400, 300 );
   }

   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );

      g.drawOval( Math.min( x1, x2 ), Math.min( y1, y2 ),
                  Math.abs( x1 - x2 ), Math.abs( y1 - y2 ) );
   }
}
