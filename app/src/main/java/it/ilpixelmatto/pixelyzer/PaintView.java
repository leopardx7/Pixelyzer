package it.ilpixelmatto.Pixelyzer;

/**
 * Created by Riccardo Russo on 27/08/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//If you are developing a reactive application, you can call GLSurfaceView.setRenderMode(RENDERMODE_WHEN_DIRTY),
//which turns off the continuous animation. Then you call GLSurfaceView.requestRender() whenever you want to re-render.

public class PaintView extends View implements GLSurfaceView.Renderer {
    private static Bitmap bMap;

    private Context c;

    private static Square square;


    public PaintView(Context content, Bitmap bitmap) {
        super(content);
        c = content;
        bMap = bitmap;
        square = new Square();

    }


    public PaintView(Context context) {
        super(context);
        square = new Square();
    }


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * Called when the surface is created or recreated.
     * <p/>
     * Called when the rendering thread
     * starts and whenever the EGL context is lost. The EGL context will typically
     * be lost when the Android device awakes after going to sleep.
     * <p/>
     * Since this method is called at the beginning of rendering, as well as
     * every time the EGL context is lost, this method is a convenient place to put
     * code to create resources that need to be created when the rendering
     * starts, and that need to be recreated when the EGL context is lost.
     * Textures are an example of a resource that you might want to create
     * here.
     * <p/>
     * Note that when the EGL context is lost, all OpenGL resources associated
     * with that context will be automatically deleted. You do not need to call
     * the corresponding "glDelete" methods such as glDeleteTextures to
     * manually delete these lost resources.
     * <p/>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param config the EGLConfig of the created surface. Can be used
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
// Load the texture for the square
        square.loadGLTexture(gl, c);

        gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
        gl.glClearDepthf(1.0f);                    //Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL);            //The Type Of Depth Testing To Do

        //Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    /**
     * Called when the surface changed size.
     * <p/>
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes.
     * <p/>
     * Typically you will set your viewport here. If your camera
     * is fixed then you could also set your projection matrix here:
     * <pre class="prettyprint">
     * void onSurfaceChanged(GL10 gl, int width, int height) {
     * gl.glViewport(0, 0, width, height);
     * // for a fixed camera, set the projection too
     * float ratio = (float) width / height;
     * gl.glMatrixMode(GL10.GL_PROJECTION);
     * gl.glLoadIdentity();
     * gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
     * }
     * </pre>
     *
     * @param gl     the GL interface. Use <code>instanceof</code> to
     *               test if the interface supports GL11 or higher interfaces.
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        if (height == 0) {                        //Prevent A Divide By Zero By
            height = 1;                        //Making Height Equal One
        }

        gl.glViewport(0, 0, width, height);    //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION);    //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix

        //Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);    //Select The Modelview Matrix
        gl.glLoadIdentity();                    //Reset The Modelview Matrix
    }

    /**
     * Called to draw the current frame.
     * <p/>
     * This method is responsible for drawing the current frame.
     * <p/>
     * The implementation of this method typically looks like this:
     * <pre class="prettyprint">
     * void onDrawFrame(GL10 gl) {
     * gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
     * //... other gl calls to render the scene ...
     * }
     * </pre>
     *
     * @param gl the GL interface. Use <code>instanceof</code> to
     *           test if the interface supports GL11 or higher interfaces.
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Reset the Modelview Matrix
        gl.glLoadIdentity();

        // Drawing
        gl.glTranslatef(0.0f, 0.0f, -5.0f);        // move 5 units INTO the screen
        // is the same as moving the camera 5 units away
//		gl.glScalef(0.5f, 0.5f, 0.5f);			// scale the square to 50%
        // otherwise it will be too large
        square.draw(gl);                        // Draw the triangle

    }


    public class Square {

        private FloatBuffer vertexBuffer;    // buffer holding the vertices
        private float vertices[] = {
                -1.0f, -1.0f, 0.0f,        // V1 - bottom left
                -1.0f, 1.0f, 0.0f,        // V2 - top left
                1.0f, -1.0f, 0.0f,        // V3 - bottom right
                1.0f, 1.0f, 0.0f            // V4 - top right
        };

        private FloatBuffer textureBuffer;    // buffer holding the texture coordinates
        private float texture[] = {
                // Mapping coordinates for the vertices
                0.0f, 1.0f,        // top left		(V2)
                0.0f, 0.0f,        // bottom left	(V1)
                1.0f, 1.0f,        // top right	(V4)
                1.0f, 0.0f        // bottom right	(V3)
        };

        /**
         * The texture pointer
         */
        private int[] textures = new int[1];

        public Square() {
            // a float has 4 bytes so we allocate for each coordinate 4 bytes
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());

            // allocates the memory from the byte buffer
            vertexBuffer = byteBuffer.asFloatBuffer();

            // fill the vertexBuffer with the vertices
            vertexBuffer.put(vertices);

            // set the cursor position to the beginning of the buffer
            vertexBuffer.position(0);

            byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            textureBuffer = byteBuffer.asFloatBuffer();
            textureBuffer.put(texture);
            textureBuffer.position(0);
        }

        /**
         * Load the texture for the square
         *
         * @param gl
         * @param context
         */
        public void loadGLTexture(GL10 gl, Context context) {
            // loading texture
            Bitmap bitmap = bMap;

            // generate one texture pointer
            gl.glGenTextures(1, textures, 0);
            // ...and bind it to our array
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

            // create nearest filtered texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            //Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Clean up
            bitmap.recycle();
        }


        /**
         * The draw method for the square with the GL context
         */
        public void draw(GL10 gl) {
            // bind the previously generated texture
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

            // Point to our buffers
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Set the face rotation
            gl.glFrontFace(GL10.GL_CW);

            // Point to our vertex buffer
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

            // Draw the vertices as triangle strip
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

            //Disable the client state before leaving
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }


}
/*
public class PaintView implements GLSurfaceView.Renderer {

    private static final String TAG = "TextureRenderer";

    private Context mContext;

    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    private boolean mEffectChanged = false;
    private boolean mOriginalImage = true;
    int mCurrentEffect = R.id.none;

    private Bitmap bitmap;
    private int mProgram;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;
    private int mPosCoordHandle;

    private FloatBuffer mTexVertices;
    private FloatBuffer mPosVertices;

    private int mViewWidth;
    private int mViewHeight;

    private int mTexWidth;
    private int mTexHeight;

    private float mZoom = 1.0f;
    private float mRot = 0.0f;

    private static final String VERTEX_SHADER =
            "attribute vec4 a_position;\n" +
                    "attribute vec2 a_texcoord;\n" +
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = a_position;\n" +
                    "  v_texcoord = a_texcoord;\n" +
                    "}\n";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "uniform sampler2D tex_sampler;\n" +
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(tex_sampler, v_texcoord);\n" +
                    "}\n";

    private static final float[] TEX_VERTICES = {
            0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
    };

    private static final float[] POS_VERTICES = {
            -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f
    };

    private static final int FLOAT_SIZE_BYTES = 4;

    public PaintView(Context appContext, Bitmap bmap) {
        mContext = appContext;
        bitmap = bmap;
    }


    public void updateTextureSize(int texWidth, int texHeight) {
        mTexWidth = texWidth;
        mTexHeight = texHeight;
        computeOutputVertices();
    }

    public void updateViewSize(int viewWidth, int viewHeight) {
        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        computeOutputVertices();
    }

    public void renderTexture(int texId) {
        // Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use our shader program
        GLES20.glUseProgram(mProgram);

        // Set viewport
        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false,
                0, mTexVertices);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false,
                0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);

        // Set the input texture
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    private void computeOutputVertices() {
        if (mPosVertices != null) {
            float imgAspectRatio = mTexWidth / (float)mTexHeight;
            float viewAspectRatio = mViewWidth / (float)mViewHeight;
            float x0, y0, x1, y1;
            if (imgAspectRatio > 1.0f) {
                x0 = -1.0f ;
                y0 = -1.0f / imgAspectRatio;
                x1 = 1.0f ;
                y1 = 1.0f / imgAspectRatio;
            } else {
                x0 = -1.0f *imgAspectRatio;
                y0 = -1.0f;
                x1 = 1.0f *imgAspectRatio;
                y1 = 1.0f;
            }
            float[] coords = new float[] { x0, y0, x1, y0, x0, y1, x1, y1 };
            // Scale coordinates with Zoom
            for (int i = 0; i < 8; i++) {
                coords[i] *= mZoom;
            }
            // Rotate coordinates
            float cosa = (float)Math.cos(mRot);
            float sina = (float)Math.sin(mRot);
            float x,y;
            for (int i = 0; i < 8; i+=2) {
                x = coords[i]; y = coords[i+1];
                coords[i]   = cosa*x-sina*y;
                coords[i+1] = sina*x+cosa*y;
            }
            // Scale screen coords
            if (viewAspectRatio > 1.0f) {
                for (int i = 0; i < 8; i+=2) {
                    coords[i] = coords[i]/viewAspectRatio;
                }
            } else {
                for (int i = 1; i < 8; i+=2) {
                    coords[i] = coords[i]*viewAspectRatio;
                }
            }
            mPosVertices.put(coords).position(0);
        }
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);


    }

    private void initEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        */
/**
 * Initialize the correct effect based on the selected menu/action item
 *//*

        switch (mCurrentEffect) {

            case R.id.none:
                break;

            case R.id.autofix:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", 0.5f);
                break;

            case R.id.bw:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", .1f);
                mEffect.setParameter("white", .7f);
                break;

            case R.id.brightness:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", 2.0f);
                break;

            case R.id.contrast:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", 1.4f);
                break;

            case R.id.crossprocess:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_CROSSPROCESS);
                break;

            case R.id.documentary:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DOCUMENTARY);
                break;

            case R.id.duotone:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_DUOTONE);
                mEffect.setParameter("first_color", Color.YELLOW);
                mEffect.setParameter("second_color", Color.DKGRAY);
                break;

            case R.id.filllight:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", .8f);
                break;

            case R.id.fisheye:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", .5f);
                break;

            case R.id.flipvert:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;

            case R.id.fliphor:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;

            case R.id.grain:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", 1.0f);
                break;

            case R.id.grayscale:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_GRAYSCALE);
                break;

            case R.id.lomoish:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_LOMOISH);
                break;

            case R.id.negative:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_NEGATIVE);
                break;

            case R.id.posterize:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_POSTERIZE);
                break;

            case R.id.rotate:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", 180);
                break;

            case R.id.saturate:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", .5f);
                break;

            case R.id.sepia:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SEPIA);
                break;

            case R.id.sharpen:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_SHARPEN);
                break;

            case R.id.temperature:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", .9f);
                break;

            case R.id.tint:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", Color.MAGENTA);
                break;

            case R.id.vignette:
                mEffect = effectFactory.createEffect(
                        EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", .5f);
                break;

            default:
                break;

        }
    }

    private void applyEffect() {
        if (mOriginalImage) {
            // Apply to original (0)
            mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
            mOriginalImage = false;
        } else {
            // Apply effect to the already modified
            mEffect.apply(mTextures[1], mImageWidth, mImageHeight, mTextures[1]);
        }
    }

    private void renderResult() {
        if (mCurrentEffect != R.id.none) {
            // render the result of applyEffect()
            renderTexture(mTextures[1]);
        }
        else {
            // render the original
            renderTexture(mTextures[0]);
        }
    }

    // Called from TouchGLView
    public void setCurrentEffect(int effectID) {
        mCurrentEffect = effectID;
        // Also indicate that the effect has changed
        mEffectChanged = true;
    }

    // Called from TouchGLView when user resets the effect
    public void resetEffect() {
        mCurrentEffect = R.id.none;
        mOriginalImage = true;
    }

    // Called from the UI when the user drags the scene.
    public void drag(float dx, float dy) {
        // Use dx to rotate the image
        mRot += dx/400.0f;
        computeOutputVertices();
    }

    // Called from the UI when the user zooms the scene.
    public void zoom(float z) {
        mZoom += z/400.0f;
        if (mZoom < 0.1f) { mZoom = 0.1f; }
        else if (mZoom > 3.0f) { mZoom = 3.0f; }
        computeOutputVertices();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!mInitialized) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            init();
            loadTextures();
            mInitialized = true;
        }
        if (mEffectChanged) {
            initEffect();
            applyEffect();
            mEffectChanged = false;
        }
        renderResult();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        updateViewSize(width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

}*/
