import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import gohai.glvideo.GLVideo;
import java.net.*;
import java.util.Arrays;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class GLVideo_Tester_Dan extends PApplet {


//import processing.io.*;
//import processing.io.GPIO;

OPC opc;
PImage im;


GLVideo video;

//import processing.sound.*;
//Amplitude rms;
//AudioIn in;
//float scale=5;             // Declare a scaling factor
//float smooth_factor=0.9;  // Declare a smooth factor
//float sum;                 // Used for smoothing

ColorWheel interfaceWin;
boolean micToggleOn = false;
boolean prevButtonOver = false;
boolean nextButtonOver = false;
boolean decBrightButtonOver = false;
boolean incBrightButtonOver = false;
int selectedColor;
int buttonPin = 20;   //added by DRC

String[] filenames;
int filenameIndex = 0;

float[] brightnessVals = {0, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1.0f};
int brightnessIndex = 10;

boolean blank = false;
int blankingCounter = 0;

//PShader maskShader;
//PGraphics maskImage;

public void settings() {
  size(160, 75, P3D);
}

public void setup()
{

  noCursor();
  initFilenameArray();

  video = new GLVideo(this, filenames[filenameIndex]);
  video.loop();
  //maskImage = createGraphics(width, height, P2D);
  //maskImage.noSmooth();
  //maskShader = loadShader("mask.glsl");
  //maskShader.set("mask", maskImage);
  //background(255);

  //rms = new Amplitude(this);
  //in = new AudioIn(this, 0);
  //in.start();
  //rms.input(in);

  interfaceWin = new ColorWheel();

  // Connect to the local instance of fcserver
  opc = new OPC(this, "127.0.0.1", 7890);
  opc.showLocations(false);

  //int wIdth = 320;
  //int HEight = 150;
  // Map one 64-LED strip to the center of the window
  //opc.ledStrip(0, 64, width/2, height/2, width / 70.0, 0, false);
  //opc.ledGrid(0, 30, 14, width/2, height/2, width/30.0, height/14.0, 0, true);
  opc.ledGrid(0, 64, 30, width/2, height/2, width/64.0f, height/30.0f, 0, false);
}

public void draw()
{
  //background( color(0) );
  this.surface.setLocation(15,203);
  //surface.setAlwaysOnTop(false);
  if (prevButtonOver) {
    loadPrevClip();
    blank = true;
    blankingCounter = 0;
    prevButtonOver = false;
  }
  else if (nextButtonOver) {
    loadNextClip();
    blank = true;
    blankingCounter = 0;
    nextButtonOver = false;
  }

  if (decBrightButtonOver) {
    brightnessIndex--;
    if (brightnessIndex < 0) {
      brightnessIndex = 0;
    }
    decBrightButtonOver = false;
  }
  else if (incBrightButtonOver) {
    brightnessIndex++;
    if (brightnessIndex >= 10) {
      brightnessIndex = 9;
    }
    incBrightButtonOver = false;
  }

  if (video.available()) {
    video.read();
  }

  int alteredColor = selectedColor;

  // the contenst will remain commented out until
  // an audio solution is found
  if (micToggleOn) {
    /*
    // smooth the rms data by smoothing factor
    sum += (rms.analyze() - sum) * smooth_factor;
    sum = rms.analyze();

    // rms.analyze() return a value between 0 and 1. It's
    // scaled to 255 and then multiplied by a scale factor
    float rms_scaled=sum*(255)*scale;

    float r = red(alteredColor);
    float g = green(alteredColor);
    float b = blue(alteredColor);
    r = r + ((avg*255000) - r);
    g = g + ((255 - avg*255000) - g);
    b = b + ((255) - b);
    alteredColor = color(r, g, b);

    println(rms_scaled);

    float r = red(selectedColor);
    float g = green(selectedColor);
    float b = blue(selectedColor);
    r = constrain((r + rms_scaled), 0, 255);
    g = constrain((g + (255 - rms_scaled)), 0, 255);
    b = constrain((b - rms_scaled), 0, 255);
    alteredColor = color(r, g, b);
    */
  }

  float r = red(alteredColor);
  float g = green(alteredColor);
  float b = blue(alteredColor);
  r = r * brightnessVals[brightnessIndex];
  g = g * brightnessVals[brightnessIndex];
  b = b * brightnessVals[brightnessIndex];
  alteredColor = color(r, g, b);

  //maskImage.beginDraw();
  //if (mousePressed || frameCount == 1) {
  //  maskImage.background(0);
  //}
  //if (mouseX != 0 && mouseY != 0) {
  //  maskImage.noStroke();
  //  maskImage.fill(255, 0, 0);
  //  maskImage.ellipse(mouseX, mouseY, 50, 50);
  //}
  //maskImage.endDraw();
  //shader(maskShader);

  tint(alteredColor);
  image(video, 0, 0, width, height);

  if (blank) {
    if (blankingCounter < 30) {  // video blank timing
      blankingCounter += 1;
      fill(0, 0, 0);
      rect(0, 0, width, height);
    }
    else {
      blankingCounter = 0;
      blank = false;
    }
  }
}

public void initFilenameArray () {
  filenames = new String[11];
  filenames[0] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped1.mp4";
  filenames[1] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped2.mp4";
  filenames[2] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped3.mp4";
  filenames[3] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped4.mp4";
  filenames[4] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped5.mp4";
  filenames[5] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped6.mp4";
  filenames[6] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped7.mp4";
  filenames[7] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped8.mp4";
  filenames[8] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped9.mp4";
  filenames[9] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped10.mp4";
  filenames[10] = "/home/pi/Desktop/GLVideo_Tester_Dan/MCMlongFlipped11.mp4";

}

public void loadNextClip() {
  //GPIO.digitalWrite (buttonPin, GPIO.HIGH);  //DRC
  filenameIndex++;
  if (filenameIndex >= filenames.length) {
    filenameIndex = 0;
  }

  video.close();
  video = new GLVideo(this, filenames[filenameIndex]);
  video.loop();

  //GPIO.digitalWrite (buttonPin, GPIO.LOW);  //DRC
  //GPIO.releasePin(buttonPin);
}

public void loadPrevClip() {

  //GPIO.digitalWrite (buttonPin, GPIO.HIGH);  //DRC
  filenameIndex--;
  if (filenameIndex < 0) {
    filenameIndex = filenames.length-1;
  }

  video.close();
  video = new GLVideo(this, filenames[filenameIndex]);
  video.loop();

  //GPIO.digitalWrite (buttonPin, GPIO.LOW);  //DRC
  //GPIO.releasePin(buttonPin);
}

static public void main(String args[]) {
  PApplet.main("GLVideo_Tester_Dan");
}

int[] buttonRow1y = new int[3];
int[] buttonRow2y = new int[2];
int buttonRow1x;
int buttonRow2x;
int buttonDiameter = 120;
int buttonColor, baseColor;
int buttonHighlight;
int currentColor;
int backgroundColor;

PImage micIcon;

PImage prevIcon;
PImage nextIcon;
PImage plusIcon;
PImage minusIcon;

PImage colorWheel;
int colorWheelX;
int colorWheelY;
int colorWheelSize;
boolean colorWheelOver = false;

//int buttonPin = 20;   //added by DRC


class ColorWheel extends PApplet {
  ColorWheel() {
    super();
    PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
  }

  public void settings() {
    size(800, 480);
    //size(800, 480);
    fullScreen();
  }

  public void setup() {
    noCursor();
    this.surface.setAlwaysOnTop(true);
    buttonColor = color(128);
    buttonHighlight = color(204);
    baseColor = color(102);
    currentColor = baseColor;
    backgroundColor = color(0);


    buttonRow1x = 75;
    buttonRow1y[0] = 80;
    buttonRow1y[1] = height/2;
    buttonRow1y[2] = height - 80;

    buttonRow2x = 225;
    buttonRow2y[0] = height/3;
    buttonRow2y[1] = buttonRow2y[0]*2;


    ellipseMode(CENTER);
    imageMode(CENTER);

    micIcon = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/mic-symbol-128.png");
    prevIcon = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/prev-symbol-128.png");
    nextIcon = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/next-symbol-128.png");
    plusIcon = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/plus-128.png");
    minusIcon = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/minus-128.png");

    colorWheel = loadImage("/home/pi/Desktop/GLVideo_Tester_Dan/color-wheel-720.jpg");
    colorWheelSize = 480;
    colorWheelY = height/2;
    colorWheelX = 2*(width/3);
  }

  public void draw() {

    background(backgroundColor);
    stroke(100);
    strokeWeight(4);

    for (int i = 0; i < 3 ; ++i) {

      if (prevButtonOver && i == 0) {
        //fill(buttonHighlight);
        fill(buttonColor);
      }
      else if (micToggleOn && i == 1) {
        //fill(buttonHighlight);
        fill(buttonColor);
      }
      else if (nextButtonOver && i == 2) {
        //fill(buttonHighlight);
        fill(buttonColor);
      }
      else {
        fill(buttonColor);
      }

      if (i != 1) { // Dont draw mic button
        ellipse(buttonRow1x, buttonRow1y[i], buttonDiameter, buttonDiameter);
      }
    }

    for (int i = 0; i < 2 ; ++i) {

      if (decBrightButtonOver && i == 0) {
        //fill(buttonHighlight);
        fill(buttonColor);
      }
      else if (incBrightButtonOver && i == 1) {
        //fill(buttonHighlight);
        fill(buttonColor);
      }
      else {
        fill(buttonColor);
      }

      ellipse(buttonRow2x, buttonRow2y[i], buttonDiameter, buttonDiameter);
    }

    image(prevIcon, buttonRow1x, buttonRow1y[0], buttonDiameter*.6f, buttonDiameter*.6f);
    image(micIcon, buttonRow1x, buttonRow1y[1], buttonDiameter*.8f, buttonDiameter*.8f);
    image(nextIcon, buttonRow1x, buttonRow1y[2], buttonDiameter*.6f, buttonDiameter*.6f);
    image(minusIcon, buttonRow2x, buttonRow2y[0], buttonDiameter*.6f, buttonDiameter*.6f);
    image(plusIcon, buttonRow2x, buttonRow2y[1], buttonDiameter*.6f, buttonDiameter*.6f);

    image(colorWheel, colorWheelX, colorWheelY, colorWheelSize, colorWheelSize);
    strokeWeight(0);

    if (mouseOverCircle(colorWheelX, colorWheelY, colorWheelSize)) {
      selectedColor = get(mouseX, mouseY);
      fill(selectedColor);
    }
    else {
      selectedColor = color(255);
      fill(255);
    }
    stroke(100);
    strokeWeight(4);
    ellipse(colorWheelX, colorWheelY, colorWheelSize*.31f, colorWheelSize*.31f);
  }


  public void mouseDownUpdate(int x, int y) {
    if ( pointOverCircle(x, y, buttonRow1x, buttonRow1y[0], buttonDiameter) ) {
      prevButtonOver = true;
    }
    else if ( pointOverCircle(x, y, buttonRow1x, buttonRow1y[1], buttonDiameter) ) {
      micToggleOn = !micToggleOn;
    }
    else if ( pointOverCircle(x, y, buttonRow1x, buttonRow1y[2], buttonDiameter) ) {
      nextButtonOver = true;
    }
    else if ( pointOverCircle(x, y, buttonRow2x, buttonRow2y[0], buttonDiameter) ) {
      decBrightButtonOver = true;
    }
    else if ( pointOverCircle(x, y, buttonRow2x, buttonRow2y[1], buttonDiameter) ) {
      incBrightButtonOver = true;
    }
  }


  public void mousePressed(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    mouseDownUpdate(x, y);
  }

  public void mouseReleased(MouseEvent event) {
    prevButtonOver = false;
    nextButtonOver = false;
    decBrightButtonOver = false;
    incBrightButtonOver = false;
  }

  public boolean mouseOverCircle(int x, int y, int diameter) {
    return pointOverCircle(mouseX, mouseY, x, y, diameter);
  }

  public boolean pointOverCircle(int pointX, int pointY, int cirX, int cirY, int diameter) {
    float disX = cirX - pointX;
    float disY = cirY - pointY;
    if (sqrt(sq(disX) + sq(disY)) < diameter/2 ) {
      return true;
    } else {
      return false;
    }
  }

  public int getSelectedColor() {
    return selectedColor;
  }

  public boolean getMicOn() {
    return micToggleOn;
  }

}
/*
 * Simple Open Pixel Control client for Processing,
 * designed to sample each LED's color from some point on the canvas.
 *
 * Micah Elizabeth Scott, 2013
 * This file is released into the public domain.
 */





public class OPC implements Runnable
{
  Thread thread;
  Socket socket;
  OutputStream output, pending;
  String host;
  int port;

  int[] pixelLocations;
  byte[] packetData;
  byte firmwareConfig;
  String colorCorrection;
  boolean enableShowLocations;

  OPC(PApplet parent, String host, int port)
  {
    this.host = host;
    this.port = port;
    thread = new Thread(this);
    thread.start();
    this.enableShowLocations = true;
    parent.registerMethod("draw",this);

  }

  // Set the location of a single LED
  public void led(int index, int x, int y)
  {
    // For convenience, automatically grow the pixelLocations array. We do want this to be an array,
    // instead of a HashMap, to keep draw() as fast as it can be.
    if (pixelLocations == null) {
      pixelLocations = new int[index + 1];
    } else if (index >= pixelLocations.length) {
      pixelLocations = Arrays.copyOf(pixelLocations, index + 1);
    }

    pixelLocations[index] = x + width * y;
  }

  // Set the location of several LEDs arranged in a strip.
  // Angle is in radians, measured clockwise from +X.
  // (x,y) is the center of the strip.
  public void ledStrip(int index, int count, float x, float y, float spacing, float angle, boolean reversed)
  {
    float s = sin(angle);
    float c = cos(angle);
    for (int i = 0; i < count; i++) {
      led(reversed ? (index + count - 1 - i) : (index + i),
        (int)(x + (i - (count-1)/2.0f) * spacing * c + 0.5f),
        (int)(y + (i - (count-1)/2.0f) * spacing * s + 0.5f));
    }
  }

  // Set the locations of a ring of LEDs. The center of the ring is at (x, y),
  // with "radius" pixels between the center and each LED. The first LED is at
  // the indicated angle, in radians, measured clockwise from +X.
  public void ledRing(int index, int count, float x, float y, float radius, float angle)
  {
    for (int i = 0; i < count; i++) {
      float a = angle + i * 2 * PI / count;
      led(index + i, (int)(x - radius * cos(a) + 0.5f),
        (int)(y - radius * sin(a) + 0.5f));
    }
  }

  // Set the location of several LEDs arranged in a grid. The first strip is
  // at 'angle', measured in radians clockwise from +X.
  // (x,y) is the center of the grid.
  public void ledGrid(int index, int stripLength, int numStrips, float x, float y,
               float ledSpacing, float stripSpacing, float angle, boolean zigzag)
  {
    float s = sin(angle + HALF_PI);
    float c = cos(angle + HALF_PI);
    for (int i = 0; i < numStrips; i++) {
      ledStrip(index + stripLength * i, stripLength,
        x + (i - (numStrips-1)/2.0f) * stripSpacing * c,
        y + (i - (numStrips-1)/2.0f) * stripSpacing * s, ledSpacing,
        angle, zigzag && (i % 2) == 1);
    }
  }

  // Set the location of 64 LEDs arranged in a uniform 8x8 grid.
  // (x,y) is the center of the grid.
  public void ledGrid8x8(int index, float x, float y, float spacing, float angle, boolean zigzag)
  {
    ledGrid(index, 8, 8, x, y, spacing, spacing, angle, zigzag);
  }

  // Should the pixel sampling locations be visible? This helps with debugging.
  // Showing locations is enabled by default. You might need to disable it if our drawing
  // is interfering with your processing sketch, or if you'd simply like the screen to be
  // less cluttered.
  public void showLocations(boolean enabled)
  {
    enableShowLocations = enabled;
  }

  // Enable or disable dithering. Dithering avoids the "stair-stepping" artifact and increases color
  // resolution by quickly jittering between adjacent 8-bit brightness levels about 400 times a second.
  // Dithering is on by default.
  public void setDithering(boolean enabled)
  {
    if (enabled)
      firmwareConfig &= ~0x01;
    else
      firmwareConfig |= 0x01;
    sendFirmwareConfigPacket();
  }

  // Enable or disable frame interpolation. Interpolation automatically blends between consecutive frames
  // in hardware, and it does so with 16-bit per channel resolution. Combined with dithering, this helps make
  // fades very smooth. Interpolation is on by default.
  public void setInterpolation(boolean enabled)
  {
    if (enabled)
      firmwareConfig &= ~0x02;
    else
      firmwareConfig |= 0x02;
    sendFirmwareConfigPacket();
  }

  // Put the Fadecandy onboard LED under automatic control. It blinks any time the firmware processes a packet.
  // This is the default configuration for the LED.
  public void statusLedAuto()
  {
    firmwareConfig &= 0x0C;
    sendFirmwareConfigPacket();
  }

  // Manually turn the Fadecandy onboard LED on or off. This disables automatic LED control.
  public void setStatusLed(boolean on)
  {
    firmwareConfig |= 0x04;   // Manual LED control
    if (on)
      firmwareConfig |= 0x08;
    else
      firmwareConfig &= ~0x08;
    sendFirmwareConfigPacket();
  }

  // Set the color correction parameters
  public void setColorCorrection(float gamma, float red, float green, float blue)
  {
    colorCorrection = "{ \"gamma\": " + gamma + ", \"whitepoint\": [" + red + "," + green + "," + blue + "]}";
    sendColorCorrectionPacket();
  }

  // Set custom color correction parameters from a string
  public void setColorCorrection(String s)
  {
    colorCorrection = s;
    sendColorCorrectionPacket();
  }

  // Send a packet with the current firmware configuration settings
  public void sendFirmwareConfigPacket()
  {
    if (pending == null) {
      // We'll do this when we reconnect
      return;
    }

    byte[] packet = new byte[9];
    packet[0] = 0;          // Channel (reserved)
    packet[1] = (byte)0xFF; // Command (System Exclusive)
    packet[2] = 0;          // Length high byte
    packet[3] = 5;          // Length low byte
    packet[4] = 0x00;       // System ID high byte
    packet[5] = 0x01;       // System ID low byte
    packet[6] = 0x00;       // Command ID high byte
    packet[7] = 0x02;       // Command ID low byte
    packet[8] = firmwareConfig;

    try {
      pending.write(packet);
    } catch (Exception e) {
      dispose();
    }
  }

  // Send a packet with the current color correction settings
  public void sendColorCorrectionPacket()
  {
    if (colorCorrection == null) {
      // No color correction defined
      return;
    }
    if (pending == null) {
      // We'll do this when we reconnect
      return;
    }

    byte[] content = colorCorrection.getBytes();
    int packetLen = content.length + 4;
    byte[] header = new byte[8];
    header[0] = 0;          // Channel (reserved)
    header[1] = (byte)0xFF; // Command (System Exclusive)
    header[2] = (byte)(packetLen >> 8);
    header[3] = (byte)(packetLen & 0xFF);
    header[4] = 0x00;       // System ID high byte
    header[5] = 0x01;       // System ID low byte
    header[6] = 0x00;       // Command ID high byte
    header[7] = 0x01;       // Command ID low byte

    try {
      pending.write(header);
      pending.write(content);
    } catch (Exception e) {
      dispose();
    }
  }

  // Automatically called at the end of each draw().
  // This handles the automatic Pixel to LED mapping.
  // If you aren't using that mapping, this function has no effect.
  // In that case, you can call setPixelCount(), setPixel(), and writePixels()
  // separately.
  public void draw()
  {
    if (pixelLocations == null) {
      // No pixels defined yet
      return;
    }
    if (output == null) {
      return;
    }

    int numPixels = pixelLocations.length;
    int ledAddress = 4;

    setPixelCount(numPixels);
    loadPixels();

    for (int i = 0; i < numPixels; i++) {
      int pixelLocation = pixelLocations[i];
           // print(i);
      int pixel = pixels[pixelLocation];


      packetData[ledAddress] = (byte)(pixel >> 16);
      packetData[ledAddress + 1] = (byte)(pixel >> 8);
      packetData[ledAddress + 2] = (byte)pixel;
      ledAddress += 3;

      if (enableShowLocations) {
        pixels[pixelLocation] = 0xFFFFFF ^ pixel;
      }
    }

    writePixels();

    if (enableShowLocations) {
      updatePixels();
    }
  }

  // Change the number of pixels in our output packet.
  // This is normally not needed; the output packet is automatically sized
  // by draw() and by setPixel().
  public void setPixelCount(int numPixels)
  {
    int numBytes = 3 * numPixels;
    int packetLen = 4 + numBytes;
    if (packetData == null || packetData.length != packetLen) {
      // Set up our packet buffer
      packetData = new byte[packetLen];
      packetData[0] = 0;  // Channel
      packetData[1] = 0;  // Command (Set pixel colors)
      packetData[2] = (byte)(numBytes >> 8);
      packetData[3] = (byte)(numBytes & 0xFF);
    }
  }

  // Directly manipulate a pixel in the output buffer. This isn't needed
  // for pixels that are mapped to the screen.
  public void setPixel(int number, int c)
  {
    int offset = 4 + number * 3;
    if (packetData == null || packetData.length < offset + 3) {
      setPixelCount(number + 1);
    }

    packetData[offset] = (byte) (c >> 16);
    packetData[offset + 1] = (byte) (c >> 8);
    packetData[offset + 2] = (byte) c;
  }

  // Read a pixel from the output buffer. If the pixel was mapped to the display,
  // this returns the value we captured on the previous frame.
  public int getPixel(int number)
  {
    int offset = 4 + number * 3;
    if (packetData == null || packetData.length < offset + 3) {
      return 0;
    }
    return (packetData[offset] << 16) | (packetData[offset + 1] << 8) | packetData[offset + 2];
  }

  // Transmit our current buffer of pixel values to the OPC server. This is handled
  // automatically in draw() if any pixels are mapped to the screen, but if you haven't
  // mapped any pixels to the screen you'll want to call this directly.
  public void writePixels()
  {
    if (packetData == null || packetData.length == 0) {
      // No pixel buffer
      return;
    }
    if (output == null) {
      return;
    }

    try {
      output.write(packetData);
    } catch (Exception e) {
      dispose();
    }
  }

  public void dispose()
  {
    // Destroy the socket. Called internally when we've disconnected.
    // (Thread continues to run)
    if (output != null) {
      println("Disconnected from OPC server");
    }
    socket = null;
    output = pending = null;
  }

  public void run()
  {
    // Thread tests server connection periodically, attempts reconnection.
    // Important for OPC arrays; faster startup, client continues
    // to run smoothly when mobile servers go in and out of range.
    for(;;) {

      if(output == null) { // No OPC connection?
        try {              // Make one!
          socket = new Socket(host, port);
          socket.setTcpNoDelay(true);
          pending = socket.getOutputStream(); // Avoid race condition...
          println("Connected to OPC server");
          sendColorCorrectionPacket();        // These write to 'pending'
          sendFirmwareConfigPacket();         // rather than 'output' before
          output = pending;                   // rest of code given access.
          // pending not set null, more config packets are OK!
        } catch (ConnectException e) {
          dispose();
        } catch (IOException e) {
          dispose();
        }
      }

      // Pause thread to avoid massive CPU load
      try {
        thread.sleep(500);
      }
      catch(InterruptedException e) {
      }
    }
  }
}
}
