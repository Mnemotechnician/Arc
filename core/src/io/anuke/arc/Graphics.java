package io.anuke.arc;

import io.anuke.arc.graphics.*;
import io.anuke.arc.Graphics.Cursor.SystemCursor;
import io.anuke.arc.graphics.g2d.BitmapFont;
import io.anuke.arc.graphics.g2d.SpriteBatch;
import io.anuke.arc.graphics.glutils.*;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.util.Disposable;

/**
 * This interface encapsulates communication with the graphics processor. Depending on the available hardware and the current
 * {@link Application} configuration, access to {@link GL20} and {@link GL30} are provided here.
 * <p>
 * If supported by the backend, this interface lets you query the available display modes (graphics resolution and color depth)
 * and change it.
 * <p>
 * This interface can be used to switch between continuous and non-continuous rendering (see
 * {@link #setContinuousRendering(boolean)}), and to explicitly {@link #requestRendering()}.
 * <p>
 * There are many more utility classes that are not directly generated by the {@link Graphics} interfaces. See {@link VertexArray}
 * , {@link VertexBufferObject}, {@link IndexBufferObject}, {@link Mesh}, {@link Shader} and {@link FrameBuffer},
 * {@link BitmapFont}, {@link Batch} and so on. All these classes are managed, meaning they don't need to be reloaded on a context
 * loss. Explore the io.anuke.arc.graphics package for more classes that might come in handy.
 * @author mzechner
 */
public abstract class Graphics implements Disposable{
    /** One global spritebatch for drawing things. */
    private SpriteBatch batch = new SpriteBatch();
    /** The last cursor used. Can be Cursor or SystemCursor.*/
    private Object lastCursor;

    /** Returns the global spritebatch instance. */
    public SpriteBatch batch(){
        return batch;
    }

    /**
     * Returns whether OpenGL ES 3.0 is available. If it is you can get an instance of {@link GL30} via {@link #getGL30()} to
     * access OpenGL ES 3.0 functionality. Note that this functionality will only be available if you instructed the
     * {@link Application} instance to use OpenGL ES 3.0!
     * @return whether OpenGL ES 3.0 is available
     */
    public abstract boolean isGL30Available();

    /** @return the {@link GL20} instance */
    public abstract GL20 getGL20();

    /** Set the GL20 instance **/
    public abstract void setGL20(GL20 gl20);

    /** @return the {@link GL30} instance or null if not supported */
    public abstract GL30 getGL30();

    /** Set the GL30 instance **/
    public abstract void setGL30(GL30 gl30);

    /** Clears the color buffer using the specified color. */
    public void clear(Color color){
        Core.gl.glClearColor(color.r, color.g, color.b, color.a);
        Core.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /** @return the width of the client area in logical pixels. */
    public abstract int getWidth();

    /** @return the height of the client area in logical pixels */
    public abstract int getHeight();

    /** @return the width of the framebuffer in physical pixels */
    public abstract int getBackBufferWidth();

    /** @return the height of the framebuffer in physical pixels */
    public abstract int getBackBufferHeight();

    /**
     * Returns the id of the current frame. The general contract of this method is that the id is incremented only when the
     * application is in the running state right before calling the {@link ApplicationListener#update()} method. Also, the id of
     * the first frame is 0; the id of subsequent frames is guaranteed to take increasing values for 2<sup>63</sup>-1 rendering
     * cycles.
     * @return the id of the current frame
     */
    public abstract long getFrameId();

    /** @return the time span between the current frame and the last frame in seconds. Might be smoothed over n frames. */
    public abstract float getDeltaTime();

    /** @return the time span between the current frame and the last frame in seconds, without smoothing **/
    public abstract float getRawDeltaTime();

    /** @return the average number of frames per second */
    public abstract int getFramesPerSecond();

    /** @return the {@link GLVersion} of this Graphics instance */
    public abstract GLVersion getGLVersion();

    /** @return the pixels per inch on the x-axis */
    public abstract float getPpiX();

    /** @return the pixels per inch on the y-axis */
    public abstract float getPpiY();

    /** @return the pixels per centimeter on the x-axis */
    public abstract float getPpcX();

    /** @return the pixels per centimeter on the y-axis. */
    public abstract float getPpcY();

    /**
     * This is a scaling factor for the Density Independent Pixel unit, following the same conventions as
     * android.util.DisplayMetrics#density, where one DIP is one pixel on an approximately 160 dpi screen. Thus on a 160dpi screen
     * this density value will be 1; on a 120 dpi screen it would be .75; etc.
     * @return the logical density of the Display.
     */
    public abstract float getDensity();

    /**
     * Whether the given backend supports a display mode change via calling {@link Graphics#setFullscreenMode(DisplayMode)}
     * @return whether display mode changes are supported or not.
     */
    public abstract boolean supportsDisplayModeChange();

    /** @return the primary monitor **/
    public abstract Monitor getPrimaryMonitor();

    /** @return the monitor the application's window is located on */
    public abstract Monitor getMonitor();

    /** @return the currently connected {@link Monitor}s */
    public abstract Monitor[] getMonitors();

    /** @return the supported fullscreen {@link DisplayMode}(s) of the monitor the window is on */
    public abstract DisplayMode[] getDisplayModes();

    /** @return the supported fullscreen {@link DisplayMode}s of the given {@link Monitor} */
    public abstract DisplayMode[] getDisplayModes(Monitor monitor);

    /** @return the current {@link DisplayMode} of the monitor the window is on. */
    public abstract DisplayMode getDisplayMode();

    /** @return the current {@link DisplayMode} of the given {@link Monitor} */
    public abstract DisplayMode getDisplayMode(Monitor monitor);

    /**
     * Sets the window to full-screen mode.
     * @param displayMode the display mode.
     * @return whether the operation succeeded.
     */
    public abstract boolean setFullscreenMode(DisplayMode displayMode);

    /**
     * Sets the window to windowed mode.
     * @param width the width in pixels
     * @param height the height in pixels
     * @return whether the operation succeeded
     */
    public abstract boolean setWindowedMode(int width, int height);

    /**
     * Sets the title of the window. Ignored on Android.
     * @param title the title.
     */
    public abstract void setTitle(String title);

    /**
     * Sets the window decoration as enabled or disabled. On Android, this will enable/disable
     * the menu bar.
     * <p>
     * Note that immediate behavior of this method may vary depending on the implementation. It
     * may be necessary for the window to be recreated in order for the changes to take effect.
     * Consult the documentation for the backend in use for more information.
     * <p>
     * Supported on all GDX desktop backends and on Android (to disable the menu bar).
     * @param undecorated true if the window border or status bar should be hidden. false otherwise.
     */
    public abstract void setUndecorated(boolean undecorated);

    /**
     * Sets whether or not the window should be resizable. Ignored on Android.
     * <p>
     * Note that immediate behavior of this method may vary depending on the implementation. It
     * may be necessary for the window to be recreated in order for the changes to take effect.
     * Consult the documentation for the backend in use for more information.
     * <p>
     * Supported on all GDX desktop backends.
     */
    public abstract void setResizable(boolean resizable);

    /**
     * Enable/Disable vsynching. This is a best-effort attempt which might not work on all platforms.
     * @param vsync vsync enabled or not.
     */
    public abstract void setVSync(boolean vsync);

    /** @return the format of the color, depth and stencil buffer in a {@link BufferFormat} instance */
    public abstract BufferFormat getBufferFormat();

    /**
     * @param extension the extension name
     * @return whether the extension is supported
     */
    public abstract boolean supportsExtension(String extension);

    /** @return whether rendering is continuous. */
    public abstract boolean isContinuousRendering();

    /**
     * Sets whether to render continuously. In case rendering is performed non-continuously, the following events will trigger a
     * redraw:
     *
     * <ul>
     * <li>A call to {@link #requestRendering()}</li>
     * <li>Input events from the touch screen/mouse or keyboard</li>
     * <li>A {@link Runnable} is posted to the rendering thread via {@link Application#post(Runnable)}. In the case
     * of a multi-window app, all windows will request rendering if a runnable is posted to the application. To avoid this,
     * post a runnable to the window instead. </li>
     * </ul>
     * <p>
     * Life-cycle events will also be reported as usual, see {@link ApplicationListener}. This method can be called from any
     * thread.
     * @param isContinuous whether the rendering should be continuous or not.
     */
    public abstract void setContinuousRendering(boolean isContinuous);

    /** Requests a new frame to be rendered if the rendering mode is non-continuous. This method can be called from any thread. */
    public abstract void requestRendering();

    /** Whether the app is fullscreen or not */
    public abstract boolean isFullscreen();

    /**
     * Create a new cursor represented by the {@link io.anuke.arc.graphics.Pixmap}. The Pixmap must be in RGBA8888 format,
     * width & height must be powers-of-two greater than zero (not necessarily equal) and of a certain minimum size (32x32 is a safe bet),
     * and alpha transparency must be single-bit (i.e., 0x00 or 0xFF only). This function returns a Cursor object that can be set as the
     * system cursor by calling {@link #setCursor(Cursor)} .
     * @param pixmap the mouse cursor image as a {@link io.anuke.arc.graphics.Pixmap}
     * @param xHotspot the x location of the hotspot pixel within the cursor image (origin top-left corner)
     * @param yHotspot the y location of the hotspot pixel within the cursor image (origin top-left corner)
     * @return a cursor object that can be used by calling {@link #setCursor(Cursor)} or null if not supported
     */
    public abstract Cursor newCursor(Pixmap pixmap, int xHotspot, int yHotspot);

    /**
     * Creates a new cursor by scaling a pixmap and adding an outline.
     * @param pixmap The base pixmap. Unscaled.
     * @param scaling The factor by which to scale the base pixmap.
     * @param outlineColor The color of the cursor's outline.
     */
    public Cursor newCursor(Pixmap pixmap, int scaling, Color outlineColor){
        Pixmap out = Pixmaps.outline(pixmap, outlineColor);
        out.setColor(Color.WHITE);
        Pixmap out2 = Pixmaps.scale(out, scaling);

        if(!Mathf.isPowerOfTwo(out2.getWidth())){
            Pixmap old = out2;
            out2 = Pixmaps.resize(out2, Mathf.nextPowerOfTwo(out2.getWidth()), Mathf.nextPowerOfTwo(out2.getWidth()));
            old.dispose();
        }

        out.dispose();
        pixmap.dispose();

        return newCursor(out2, out2.getWidth() / 2, out2.getHeight() / 2);
    }

    /**
     * Creates a new cursor by file name.
     * @param filename the name of the cursor .png file, found in the internal file "cursors/{name}.png"
     */
    public Cursor newCursor(String filename, int scaling, Color outlineColor){
        return newCursor(new Pixmap(Core.files.internal("cursors/" + filename + ".png")), scaling, outlineColor);
    }

    /**Sets the cursor to the default value, e.g. {@link SystemCursor#arrow}.*/
    public void restoreCursor(){
        setSystemCursor(SystemCursor.arrow);
    }

    /**Sets the display cursor.*/
    public void cursor(Cursor cursor){
        if(lastCursor == cursor) return;

        if(cursor instanceof SystemCursor){
            if(((SystemCursor)cursor).cursor != null){
                setCursor(((SystemCursor)cursor).cursor);
            }else{
                setSystemCursor((SystemCursor)cursor);
            }
        }else{
            setCursor(cursor);
        }

        lastCursor = cursor;
    }

    /**
     * Only viable on the lwjgl-backend and on the gwt-backend. Browsers that support cursor:url() and support the png format (the
     * pixmap is converted to a data-url of type image/png) should also support custom cursors. Will set the mouse cursor image to
     * the image represented by the {@link Cursor}. It is recommended to call this function in the main render thread, and maximum one time per frame.
     * Internal use only!
     * @param cursor the mouse cursor as a {@link Cursor}
     */
    protected abstract void setCursor(Cursor cursor);

    /**Sets one of the predefined {@link SystemCursor}s.
     * Internal use only!*/
    protected abstract void setSystemCursor(SystemCursor systemCursor);

    @Override
    public void dispose(){
        if(batch != null){
            batch.dispose();
            batch = null;
        }

        for(SystemCursor cursor : SystemCursor.values()){
            cursor.dispose();
        }
    }

    /**
     * Describe a fullscreen display mode
     * @author mzechner
     */
    public static class DisplayMode{
        /** the width in physical pixels **/
        public final int width;
        /** the height in physical pixles **/
        public final int height;
        /** the refresh rate in Hertz **/
        public final int refreshRate;
        /** the number of bits per pixel, may exclude alpha **/
        public final int bitsPerPixel;

        protected DisplayMode(int width, int height, int refreshRate, int bitsPerPixel){
            this.width = width;
            this.height = height;
            this.refreshRate = refreshRate;
            this.bitsPerPixel = bitsPerPixel;
        }

        public String toString(){
            return width + "x" + height + ", bpp: " + bitsPerPixel + ", hz: " + refreshRate;
        }
    }

    /**
     * Describes a monitor
     * @author badlogic
     */
    public static class Monitor{
        public final int virtualX;
        public final int virtualY;
        public final String name;

        protected Monitor(int virtualX, int virtualY, String name){
            this.virtualX = virtualX;
            this.virtualY = virtualY;
            this.name = name;
        }
    }

    /** Class describing the bits per pixel, depth buffer precision, stencil precision and number of MSAA samples. */
    public static class BufferFormat{
        /* number of bits per color channel */
        public final int r, g, b, a;
        /* number of bits for depth and stencil buffer */
        public final int depth, stencil;
        /** number of samples for multi-sample anti-aliasing (MSAA) **/
        public final int samples;
        /** whether coverage sampling anti-aliasing is used. in that case you have to clear the coverage buffer as well! */
        public final boolean coverageSampling;

        public BufferFormat(int r, int g, int b, int a, int depth, int stencil, int samples, boolean coverageSampling){
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.depth = depth;
            this.stencil = stencil;
            this.samples = samples;
            this.coverageSampling = coverageSampling;
        }

        public String toString(){
            return "r: " + r + ", g: " + g + ", b: " + b + ", a: " + a + ", depth: " + depth + ", stencil: " + stencil
            + ", num samples: " + samples + ", coverage sampling: " + coverageSampling;
        }
    }

    /**
     * <p>
     * Represents a mouse cursor. Create a cursor via
     * {@link Graphics#newCursor(Pixmap, int, int)}. To
     * set the cursor use {@link Graphics#setCursor(Cursor)}.
     * To use one of the system cursors, call Graphics#setSystemCursor
     * </p>
     **/
    public interface Cursor extends Disposable{

        enum SystemCursor implements Cursor{
            arrow,
            ibeam,
            crosshair,
            hand,
            horizontalResize,
            verticalResize;

            /**The override cursor to use when setting this cursor.*/
            private Cursor cursor;

            /**Sets the alias for this cursor.*/
            public void set(Cursor cursor){
                this.cursor = cursor;
            }

            @Override
            public void dispose(){
                if(cursor != null && !(cursor instanceof SystemCursor)){
                    cursor.dispose();
                    cursor = null;
                }
            }
        }
    }
}
