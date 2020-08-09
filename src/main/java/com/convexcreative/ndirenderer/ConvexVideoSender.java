package com.convexcreative.ndirenderer;

import com.walker.devolay.DevolaySender;
import com.walker.devolay.DevolayVideoFrame;

public class ConvexVideoSender extends DevolaySender {

    public ConvexVideoSender(String ndiName){
        super(ndiName);
    }

    public void renderConvexFrame(DevolayVideoFrame frame){


        /// blah blah
        sendVideoFrameAsync(frame);
    }
}
