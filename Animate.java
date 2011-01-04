/*
  Copyright (c) 2011 Timothy Lovorn

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.

  -- ^^^ MIT license
  -- quoted sections in comments are from Java 1.6 API
*/

import java.applet.*;
import java.awt.*;

public class Animate extends Applet implements Runnable {
    final int fps = 24, totalFrames, waitTime;
    Thread animator = null;
    bool animatorSuspended;
    ArrayList<Image> imageList;
    int frameNumber = 0;
    
    // constructor for applet
    public void init() {
        URL imageBase = URL(getCodeBase(), "rickroll");
        loadImages(imageBase, "rickroll", 24);
    }

    // destructor for applet
    public void destroy() {

    }

    // executes after init() and also "each time the applet is revisited"
    public void start() {
        if (animator == null) {
            animator = new Thread(this);
            animatorSuspended = false;
            animator.start();
        }
        else if (animatorSuspended) {
            animatorSuspended = false;
            // only one thread at a time in this block
            synchronized (this) {
                 // restart the thread ("wakes up a single thread that is 
                 // waiting on this object's monitor")
                 notify(); 
            }
        }
    }

    // executes "when the Web page that contains this applet has been replaced
    // by another page, and also just before the applet is to be destroyed."
    public void stop() {
        animatorSuspended = true;
    }

    // main thread (required by Runnable)
    public void run() {
        try {
            while (true) {
                // if the thread is suspended, wait until notify
                if (animatorSuspended) {
                    synchronized (this) {
                        while (animatorSuspended) {
                            wait();
                        }
                    }
                }
                // advance the frame number, roll over to 0 at numFrames
                frameNumber = (frameNumber + 1) % totalFrames;
                // paint frame
                repaint();
                // don't go to next frame immediately
                animator.sleep(waitTime);
            }
        }
        catch (InterruptedException e) { }
    }

    // called on redraw
    public void paint() {

    }

    private void loadImages(URL base, String prefix, int numToLoad) {
        imageList = new ArrayList<Image>;
        for (int index = 0; index < numToLoad; index++) {
            Image img = getImage(base, prefix + String.valueOf(index));
            imageList.add(img);
        }
        totalFrames = numToLoad;
        waitTime = totalFrames / fps;
    }
}