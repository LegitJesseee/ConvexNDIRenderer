package com.convexcreative.ndirenderer;

public class Main {

    public static void main(String... args){

        ConvexRenderEngine renderEngine = new ConvexRenderEngine(1920, 1080, 60, "Epic Feed").initialize();
        renderEngine.getTaskManager().registerTask(new DrawTestTask(renderEngine), 30);

    }

}

