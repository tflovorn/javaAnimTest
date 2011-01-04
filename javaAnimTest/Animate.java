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

package javaAnimTest;
import javaAnimTest.*;

import java.applet.*;
import java.awt.*;
import java.net.*;
import java.util.ArrayList;

public class Animate extends Applet implements Runnable {
    final int fps = 24;
    Thread animator = null;
    ArrayList<Image> imageList;
    int frameNumber = 0, totalFrames, waitTime;
    Logger debugLog;
    
    // constructor for applet
    public void init() {
        debugLog = new Logger("debug.txt");
        debugLog.writeln("init starting");
        try {
            URL imageBase = new URL(getDocumentBase(), "rickroll"); 
            loadImages(imageBase, "rickroll", "png", 24);
        } catch (MalformedURLException e) { 
            debugLog.writeln(e.toString());
        }
    }

    // destructor for applet
    public void destroy() {
        debugLog.close();
    }

    // executes after init() and also "each time the applet is revisited"
    public void start() {
        animator = new Thread(this);
        animator.start();
    }

    // executes "when the Web page that contains this applet has been replaced
    // by another page, and also just before the applet is to be destroyed."
    public void stop() {
        animator = null;
    }

    // main thread (required by Runnable)
    public void run() {
        long time = System.currentTimeMillis();
        while (Thread.currentThread() == animator) {
            // paint frame
            repaint();
            // don't go to next frame immediately
            try {
                time += waitTime;
                Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
            } catch (InterruptedException e) { 
                break;
            }
            // advance the frame number, roll over to 0 at numFrames
            frameNumber = (frameNumber + 1) % totalFrames;
        }     

    }

    // called on redraw
    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        Dimension dims = getSize();
        g.setColor(Color.black);
        g.fillRect(0, 0, dims.width, dims.height);
        g.drawImage(imageList.get(frameNumber), 0, 0, null);
    }

    // load specified series of images into imageList
    private void loadImages(URL base, String prefix, String extension, 
                            int numToLoad) {
        imageList = new ArrayList<Image>();
        for (int index = 0; index < numToLoad; index++) {
            String fileName = prefix + String.valueOf(index) + extension;
            Image img = getImage(base, fileName);
            imageList.add(img);
        }
        totalFrames = numToLoad;
        waitTime = 1000 * totalFrames / fps;
    }
}
