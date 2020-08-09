package com.convexcreative.ndirenderer;

import com.walker.devolay.Devolay;
import com.walker.devolay.DevolayFrameFourCCType;
import com.walker.devolay.DevolayVideoFrame;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ConvexRenderEngine {

    private int width;
    private int height;
    private int framerate;
    private String instanceName;

    private BufferedImage canvas;

    private ConvexTaskManager taskManager;

    private boolean initialized = false;

    private DevolayVideoFrame videoFrame;
    private ConvexVideoSender videoSender;

    private  ByteBuffer[] frameBuffers;

    public ConvexRenderEngine(int width, int height, int framerate, String instanceName){
        this.width = width;
        this.height = height;
        this.framerate = framerate;
        this.instanceName = instanceName;

    }

    public ConvexRenderEngine initialize(){
        System.out.println("- Loading Devolay Libraries...");
        Devolay.loadLibraries();
        videoSender = new ConvexVideoSender(instanceName);
        final int pixelDepth = 4;
        videoFrame = new DevolayVideoFrame();
        videoFrame.setResolution(width, height);
        videoFrame.setFourCCType(DevolayFrameFourCCType.BGRA);
        videoFrame.setLineStride(width * pixelDepth);
        videoFrame.setFrameRate(framerate, 1);


        frameBuffers = new ByteBuffer[2];
        frameBuffers[0] = ByteBuffer.allocateDirect(width * height * 4);
        frameBuffers[1] = ByteBuffer.allocateDirect(width* height * 4);

        canvas = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);

        taskManager = new ConvexTaskManager(this);

        taskManager.initialize();

        System.out.println("Render Engine Initialization Complete!");
        initialized = true;
        return this;
    }

    public boolean close(){
        if(initialized) {
            videoFrame.close();
            videoSender.close();
            taskManager.close();
            initialized = false;
            return true;
        }
        return false;
    }

    public boolean reinitialize(){
        if(initialized){
            close();
            initialize();
            return true;
        }
        return false;
    }

    // GETTERS & SETTERS


    public int getFrameWidth(){
        return width;
    }

    public int getFrameHeight(){
        return height;
    }

    public int getFPS(){
        return framerate;
    }

    public String getInstanceName(){
        return instanceName;
    }

    public BufferedImage getCanvas(){
        return canvas;
    }

    public ConvexTaskManager getTaskManager(){
        return taskManager;
    }


    public void renderNewFrame(){
        ByteBuffer buffer = frameBuffers[ConvexRenderEngine.boolIndex(taskManager.getCurrentFrame())];
        generateImageBuffer(buffer, canvas);
        DevolayVideoFrame frame = videoFrame;
        frame.setData(buffer);
        videoSender.sendVideoFrameAsync(frame);
    }


    private void generateImageBuffer(ByteBuffer buffer, BufferedImage img) {

        final int dataTotal = img.getWidth() * img.getHeight() * 4;
        final int bufferCapacity = buffer.capacity();
        final byte maxAlpha = (byte) 255;

        buffer.position(0);
      //  System.out.println("dataTotal/bufferCapacity (" + dataTotal + " / " + bufferCapacity + ")");

        if(dataTotal > bufferCapacity){
            System.out.println("Could not render image! Image is bigger than buffer capacity. (" + dataTotal + " / " + bufferCapacity + ")");
            return;
        }

        Arrays.stream(getPixels(img, getFrameWidth())).forEach(pixel -> {

            byte b = (byte) ((pixel)&0xFF);
            byte r = (byte) ((pixel>>8)&0xFF);
            byte g = (byte) ((pixel>>16)&0xFF);
            byte a = (byte) ((pixel>>24)&0xFF);

            buffer.put(b).put(r).put(g).put(a);


        });



        buffer.flip();
    }

   private static int[] getPixels(BufferedImage img, int frameWidth){
        final int HEIGHT = img.getHeight();
        final int WIDTH = img.getWidth();
        return img.getRGB(0,0,WIDTH,HEIGHT,new int[WIDTH*HEIGHT],0,frameWidth);
   }


   // STATIC

    public static int boolIndex(int num){

        if(num % 2 == 0){
            return 0;
        }else{
            return 1;
        }
    }

}
