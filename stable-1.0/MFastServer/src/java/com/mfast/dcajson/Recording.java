/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.dcajson;

/**
 *
 * @author saqibahmad
 */
public class Recording {
    int frameCount;
    SkeletonSequence[] skeletonSequence;

    public int getFrameCount() {
        return frameCount;
    }

    public SkeletonSequence[] getSkeletonSequence() {
        return skeletonSequence;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public void setSkeletonSequence(SkeletonSequence[] skeletonSequence) {
        this.skeletonSequence = skeletonSequence;
    }
}
