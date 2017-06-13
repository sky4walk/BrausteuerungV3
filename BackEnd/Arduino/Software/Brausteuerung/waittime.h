// brausteuerung@AndreBetz.de
#ifndef __WAITTIME__
#define __WAITTIME__

#include <Arduino.h>
///////////////////////////////////////////////////////////////////////////////
// wait timer
///////////////////////////////////////////////////////////////////////////////
#define SECPROMIN 	   60
#define MIL2SEC		   1000

class WaitTime
{
  public:
    WaitTime(unsigned long interval)
    {
      mWaitTime = interval;
      init();
    }
    void init()
    {
      mStartTime = 0;
      mLastStart = 0;
      mInitialized = false;
      mContinue    = false;
      mPause       = false;
    }
    void init(unsigned long interval)
    {
      mStartTime = 0;
      mLastStart = interval;
      mInitialized = false;
      mPause       = false;
      mContinue    = true;
    }
    void start(unsigned long interval)
    {
      if ( mContinue )
      {
        mWaitTime = mLastStart;
      }
      else
        mWaitTime = interval;
      start();
    }
    void start()
    {
      if ( false == mInitialized )
      {
        mStartTime = millis();
        mInitialized = true;
      }
    }
    void pause()
    {
      mPause = true;
      mDuration = millis() - mStartTime;
    }
    void resume()
    {
      mPause = false;
      mStartTime = millis() - mDuration;
    }
    boolean timeOver()
    {
      if ( false == mPause )
      {
        unsigned long actTime = millis();
        mDuration = actTime - mStartTime;
        if ( mDuration >= mWaitTime )
        {
          mDuration = mWaitTime;
          return true;
        }
      }
      return false;
    }
    unsigned long getDuration()
    {
      return mWaitTime - mDuration;
    }
    boolean timerStarted()
    {
      mInitialized;
    }
    boolean isPause()
    {
      return mPause;
    }
  private:
    unsigned long mStartTime;
    unsigned long mWaitTime;
    unsigned long mDuration;
    unsigned long mLastStart;
    boolean mInitialized;
    boolean mContinue;
    boolean mPause;
};

#endif

