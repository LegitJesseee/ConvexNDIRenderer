package com.convexcreative.ndirenderer;

public abstract class ConvexRenderTask implements Runnable {

    private ConvexRenderEngine renderEngine;
    private int frequency;

    public ConvexRenderTask(ConvexRenderEngine renderEngine){
        this.renderEngine = renderEngine;
    }

    public ConvexRenderTask setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public int getFrequency(){
        return frequency;
    }

    public ConvexRenderEngine getRenderEngine(){
        return renderEngine;
    }
}
