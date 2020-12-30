package com.nikmlnkr.my3dgame;

/**
 * Created by nikhilmalankar on 05/03/17.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    Triangle triangle;
    private Pyramid pyramid;    // (NEW)

    // Rotational angle and speed (NEW)
    private float angleTriangle = 0.0f;
    private float speedTriangle = 0.5f;

    private static float anglePyramid = 0; // Rotational angle in degree for pyramid (NEW)
    private static float speedPyramid = 2.0f; // Rotational speed for pyramid (NEW)

    // Constructor
    public MyGLRenderer(Context context) {
        // Set up the data-array buffers for these shapes
        triangle = new Triangle();
        pyramid = new Pyramid();   // (NEW)
    }

    // Call back when the surface is first created or re-created.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // NO CHANGE - SKIP
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance

        // You OpenGL|ES initialization code here
        // ......
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // NO CHANGE - SKIP
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers using clear-values set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();                 // Reset model-view matrix ( NEW )
        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the screen ( NEW )
        gl.glRotatef(angleTriangle, 0.0f, 1.0f, 0.0f); // Rotate the triangle about the y-axis (NEW)
        triangle.draw(gl);                   // Draw triangle ( NEW )

        // ----- Render the Color Cube -----
        gl.glLoadIdentity();                // Reset the model-view matrix
        gl.glTranslatef(1.5f, 0.0f, -6.0f); // Translate right and into the screen
        gl.glRotatef(anglePyramid, 0.1f, 1.0f, -0.1f); // Rotate (NEW)
        pyramid.draw(gl);                              // Draw the pyramid (NEW)

        // Update the rotational angle after each refresh (NEW)
        angleTriangle += speedTriangle; // (NEW)
        anglePyramid += speedPyramid;   // (NEW)
    }
}