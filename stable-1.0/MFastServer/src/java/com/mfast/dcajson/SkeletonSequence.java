package com.mfast.dcajson;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author saqibahmad
 */
public class SkeletonSequence {
    int frameNumber;
    int skeletonCount;
    Skeleton[] skeletons;
    float timeStamp;
    int realTimeStampSeconds;
    int realTimeStampMicros;
    float version;
    
    public void setSkeletons(Skeleton[] sks){
        skeletons = sks;
    }
    
    public Skeleton[] getSkeletons(){
        return skeletons;
    }
    
    public int getFrameNumber(){
        return frameNumber;
    }
    
    public void setFrameNumber(int num){
        frameNumber = num;
    }
    
    public int getSkeletonCount(){
        return skeletonCount;
    }
    
    public void setSkeletonCount(int num){
        skeletonCount = num;
    }
    
    public float getTimeStamp(){
        return timeStamp;
    }
    
    public void setTimeStamp(float stamp){
        timeStamp = stamp;
    }
    
    public int getRealTimeStampSeconds() {
        return realTimeStampSeconds;
    }
    
    public void setRealTimeStampSeconds(int seconds) {
        realTimeStampSeconds = seconds;
    }
    
    public int getRealTimeStampMicros() {
        return realTimeStampMicros;
    }
    
    public void setRealTimeStampMicros(int useconds) {
        realTimeStampMicros = useconds;
    }
    
}
