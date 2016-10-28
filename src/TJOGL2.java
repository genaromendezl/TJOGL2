/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gmendez
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;        
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants
 
/**
 * JOGL 2.0 Program Template (GLCanvas)
 * This is a "Component" which can be added into a top-level "Container".
 * It also handles the OpenGL events to render graphics.
 */
@SuppressWarnings("serial")
public class TJOGL2 extends GLCanvas implements GLEventListener, KeyListener {
   // Define constants for the top-level container
   private static String TITLE = "JOGL 2.0 Setup (GLCanvas)";  // window's title
   private static final int CANVAS_WIDTH = 640;  // width of the drawable
   private static final int CANVAS_HEIGHT = 480; // height of the drawable
   private static final int FPS = 60; // animator's target frames per second
   private static final float factInc = 5.0f; // animator's target frames per second
   float fovy=45.0f;
   int   eje=0;
   float rotX=0.0f;
   float rotY=0.0f;
   float rotZ=0.0f;
 
   /** The entry main() method to setup the top-level container and animator */
   public static void main(String[] args) {
      // Run the GUI codes in the event-dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            // Create the OpenGL rendering canvas
            GLCanvas canvas = new TJOGL2();
            canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
 
            // Create a animator that drives canvas' display() at the specified FPS.
            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
 
            // Create the top-level container
            final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            
            FlowLayout fl = new FlowLayout();
            frame.setLayout(fl);
            
            panel1.add(canvas);
            frame.getContentPane().add(panel1);
            frame.getContentPane().add(panel2);
            frame.addKeyListener((KeyListener)canvas);
            frame.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosing(WindowEvent e) {
                  // Use a dedicate thread to run the stop() to ensure that the
                  // animator stops before program exits.
                  new Thread() {
                     @Override
                     public void run() {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                     }
                  }.start();
               }
            });
                        
            frame.setTitle(TITLE);
            frame.pack();
            frame.setVisible(true);
            animator.start(); // start the animation loop
         }
      });
   }
 
   // Setup OpenGL Graphics Renderer
 
   private GLU glu;  // for the GL Utility
   private GLUT glut;
 
   /** Constructor to setup the GUI for this Component */
   public TJOGL2() {
      this.addGLEventListener(this);
      this.addKeyListener(this);
   }
 
   // ------ Implement methods declared in GLEventListener ------
 
   /**
    * Called back immediately after the OpenGL context is initialized. Can be used
    * to perform one-time initialization. Run only once.
    */
   @Override
   public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
      glu = new GLU();                        // get GL Utilities
      glut = new GLUT();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
      gl.glClearDepth(1.0f);      // set clear depth value to farthest
      gl.glEnable(GL_DEPTH_TEST); // enables depth testing
      gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
      gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
      gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
 
      // ----- Your OpenGL initialization code here -----
   }
 
   /**
    * Call-back handler for window re-size event. Also called when the drawable is
    * first set to visible.
    */
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
 
      if (height == 0) height = 1;   // prevent divide by zero
      float aspect = (float)width / height;
 
      // Set the view port (display area) to cover the entire window
      gl.glViewport(0, 0, width, height);
 
      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
      gl.glLoadIdentity();             // reset projection matrix
      glu.gluPerspective(fovy, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar
 
      // Enable the model-view transform
      gl.glMatrixMode(GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }
 
   /**
    * Called back by the animator to perform rendering.
    */
   @Override
   public void display(GLAutoDrawable drawable) {
      float aspect = (float)this.getWidth() / this.getHeight();
      GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
      gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

      
      gl.glLoadIdentity();  // reset the model-view matrix
      gl.glEnable(GL.GL_LINE_SMOOTH);
      gl.glEnable(GL.GL_BLEND);
        
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
      gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
      gl.glLineWidth(1.5f);
   
      glu.gluLookAt(0.0, 0.0, 20.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
     
      if (rotX<0) rotX=360-factInc;
      if (rotY<0) rotY=360-factInc;
      if (rotZ<0) rotZ=360-factInc;

      if (rotX>=360) rotX=0;
      if (rotY>=360) rotY=0;
      if (rotZ>=360) rotZ=0;
      
      // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
                 
      gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
      gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
      gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
      gl.glColor3f(0.5f, 0.5f, 0.5f);
      
      glut.glutWireTeapot(5);
      
      
      /*
      gl.glColor3f(0.5f, 0.5f, 0.5f);       
      
      // Dibujar los ejes
      gl.glBegin(GL_LINES);         
         gl.glVertex3f(-10.0f, 0.0f, 0.0f);         
         gl.glVertex3f(10.0f, 0.0f, 0.0f);        
      gl.glEnd();

      gl.glBegin(GL_LINES);
         gl.glVertex3f(0.0f, -10.0f, 0.0f);         
         gl.glVertex3f(0.0f, 10.0f, 0.0f);        
      gl.glEnd();

      gl.glBegin(GL_LINES);
         gl.glVertex3f(0.0f, 0.0f, -10.0f);         
         gl.glVertex3f(0.0f, 0.0f, 10.0f);        
      gl.glEnd();
      
      if (rotX<0) rotX=360-factInc;
      if (rotY<0) rotY=360-factInc;
      if (rotZ<0) rotZ=360-factInc;

      if (rotX>=360) rotX=0;
      if (rotY>=360) rotY=0;
      if (rotZ>=360) rotZ=0;
      
      // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
      gl.glTranslatef(0.0f, 0.0f, -10.0f); // translate into the screen            
      gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
      gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
      gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);

      drawPyramid(0,0,0,4.0f,4.0f,gl); 

      //gl.glLoadIdentity();
      gl.glTranslatef(2.0f, 0.0f, -15.0f); // translate into the screen            
      gl.glRotatef(rotX, 1.0f, 0.0f, 0.0f);
      gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);
      gl.glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
      
      drawPyramid(0,0,0,4.0f,4.0f,gl);                  
      */
      
      gl.glFlush();
      
      
      
   }
 
   
   void drawPyramid(float x,float y,float z,float w,float h,GL2 gl){
      float mw=w/2;
      float mh=h/2;
      float xi,xd,ys,yi,zf,zp;
      
      xi=x-mw;
      xd=x+mw;
              
      yi=y-mh;
      ys=y+mh;
              
      zf=z+mw;
      zp=z-mw;              
                   
      gl.glBegin(GL_TRIANGLES); // draw using triangles
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xi, yi, zf);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);                 
         gl.glColor3f(0,0,1.0f);
         gl.glVertex3f(xd, yi, zf);
      gl.glEnd();
      
      gl.glBegin(GL_TRIANGLES); // draw using triangles
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xd,yi,zf);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xd, yi,zp);
      gl.glEnd();
      gl.glBegin(GL_TRIANGLES); // draw using triangles
         gl.glColor3f(1.0f,0.0f,0.0f);
         gl.glVertex3f(xd, yi, zp);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys,z);
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xi, yi, zp);
      gl.glEnd();
      gl.glBegin(GL_TRIANGLES); // draw using triangles
         gl.glColor3f(0.0f,0,1.0f);
         gl.glVertex3f(xi,yi, zp);
         gl.glColor3f(0,1.0f,0);
         gl.glVertex3f(x, ys, z);
         gl.glColor3f(1.0f,0,0.0f);
         gl.glVertex3f(xi, yi, zf);
      gl.glEnd();
      gl.glBegin(GL_QUADS ); // draw using triangles
         gl.glColor3f(1.0f,0,0);
         gl.glVertex3f(xi, yi, zf);
         gl.glColor3f(0,0.0f,1.0f);
         gl.glVertex3f(xd, yi, zf);
         gl.glColor3f(1.0f,0,0.0f);
         gl.glVertex3f(xd, yi, zp);
         gl.glColor3f(0.0f,0.0f,1.0f);
         gl.glVertex3f(xi, yi, zp);
      gl.glEnd();          
   }
   
   /**
    * Called back before the OpenGL context is destroyed. Release resource such as buffers.
    */
   @Override
   public void dispose(GLAutoDrawable drawable) { }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int cod = e.getKeyCode();
        switch(cod){
            case KeyEvent.VK_F1:
                    fovy+=factInc; break;
            case KeyEvent.VK_F2:
                    fovy-=factInc; break;   
            case KeyEvent.VK_F3:
                switch(eje){
                    case 1:
                        rotX+=factInc; break;
                    case 2:
                        rotY+=factInc; break;
                    case 3:
                        rotZ+=factInc; break;                        
                }
                break;
            case KeyEvent.VK_F4:
                switch(eje){
                    case 1:
                        rotX-=factInc; break;
                    case 2:
                        rotY-=factInc; break;
                    case 3:
                        rotZ-=factInc; break;                        
                } 
                break;

            case KeyEvent.VK_X:
                    eje=1;
                    break;                               
            case KeyEvent.VK_Y:
                    eje=2;
                    break;                   
            case KeyEvent.VK_Z:
                    eje=3;
                    break;                                   
        }
                        
        System.out.println("Typed="+e.getKeyCode()+", fovy="+fovy+", eje="+eje+" ,rotX="+rotX+" ,rotY="+rotY+" ,rotZ="+rotZ);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}