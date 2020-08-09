package com.convexcreative.ndirenderer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;

public class ConvexTaskManager extends Thread {

    private int currentFrame = 1;
    final private static Random RANDOM = new Random();

    private HashMap<Integer,ConvexRenderTask> registeredTasks;
    private ConvexRenderEngine renderEngine;

    private  ByteBuffer[] frameBuffers;

    public ConvexTaskManager(ConvexRenderEngine renderEngine){
        this.renderEngine = renderEngine;
    }

    public void initialize(){
        System.out.println("Initializing Task Manager...");
        registeredTasks = new HashMap<>();

        final int WIDTH = getRenderEngine().getFrameWidth();
        final int HEIGHT = getRenderEngine().getFrameHeight();


        start();
        System.out.println("Task manager initialized!");
    }

    public void close(){
        interrupt();
    }

    @Override
    public void run(){

        System.out.println("Secondary task thread started.");

        while(true){

            registeredTasks.forEach((key,task) -> {
                if(currentFrame % task.getFrequency() == 0){
                    task.run();
                }
            });

            // render

           renderEngine.renderNewFrame();
           currentFrame++;
        }

    }

    public int registerTask(ConvexRenderTask task, int frequency){
        int key;
        do{
            key = RANDOM.nextInt(100000);
        }while(registeredTasks.containsKey(key));
        registeredTasks.put(key, task.setFrequency(frequency));
        return key;
    }

    public boolean unregisterTask(int key){
        if(registeredTasks.containsKey(key)){
            registeredTasks.remove(key);
            return true;
        }
        return false;
    }

    public ConvexRenderTask getTask(int key){
        return registeredTasks.get(key);
    }

    public boolean containsTask(int key){
        return registeredTasks.containsKey(key);
    }

    public ConvexRenderEngine getRenderEngine() {
        return renderEngine;
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

}
