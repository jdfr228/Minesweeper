import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Painter extends JFrame {
	
	private int xValue = -10, yValue = -10;

	
	private class handler extends MouseMotionAdapter {
		 public void mouseDragged( MouseEvent e )
         {
            xValue = e.getX();
            yValue = e.getY();
            repaint();
         }           
	}
	
	
	   public Painter()
	   {
	      super( "A simple paint program" );

	      getContentPane().add(
	         new Label( "Drag the mouse to draw" ),
	         BorderLayout.SOUTH );

	      addMouseMotionListener(
	    		  new handler()); 
	      /*
	         new MouseMotionAdapter() {
	            public void mouseDragged( MouseEvent e )
	            {
	               xValue = e.getX();
	               yValue = e.getY();
	               repaint();
	            }           
	         }
	      );*/
	      setSize( 300, 150 );  
	      
	   }

	   public void paint( Graphics g )
	   {
	      g.fillOval( xValue, yValue, 4, 4 );
	   }

	   public static void main( String args[] )
	   {
	      Painter app = new Painter();

	      app.addWindowListener(
	         new WindowAdapter() {
	            public void windowClosing( WindowEvent e )
	            {
	               System.exit( 0 );
	            }
	         }
	      );
	     // app.show();
	      app.setSize(new Dimension(400, 300));
	      
	      app.setVisible(true);
	      
	   }
}


