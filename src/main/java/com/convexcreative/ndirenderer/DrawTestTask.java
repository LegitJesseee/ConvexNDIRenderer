package com.convexcreative.ndirenderer;

import java.awt.*;

public class DrawTestTask extends ConvexRenderTask {
    public DrawTestTask(ConvexRenderEngine instance) {
        super(instance);
    }

    @Override
    public void run() {
        final ConvexRenderEngine instance = getRenderEngine();
        final Graphics g = instance.getCanvas().getGraphics();
        final int curFrame = instance.getTaskManager().getCurrentFrame();

        getRenderEngine().getCanvas().getGraphics().clearRect(0,0, getRenderEngine().getFrameWidth(), getRenderEngine().getFrameHeight());


        g.setColor(Color.RED);
        g.fillRect(0,0, getRenderEngine().getFrameWidth(), getRenderEngine().getFrameHeight());
        String key = "Current Frame: " + curFrame;
        g.setFont(new Font("SansSerif", Font.BOLD, 100));
        g.setColor(Color.BLUE);
        g.drawString(key, (instance.getFrameWidth() / 2) - 100, instance.getFrameHeight() / 2);


        //g.dispose();

        System.out.println("Rendered " + curFrame);
    }
}
