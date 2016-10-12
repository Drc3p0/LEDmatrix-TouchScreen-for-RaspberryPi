
//import processing.io.*;
//import processing.io.GPIO;

OPC opc;
PImage im;

import gohai.glvideo.GLVideo;
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
color selectedColor;
int buttonPin = 20;   //added by DRC

String[] filenames;
int filenameIndex = 0;

float[] brightnessVals = {0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0};
int brightnessIndex = 10;

boolean blank = false;
int blankingCounter = 0;

//PShader maskShader;
//PGraphics maskImage;

public void settings() {
  size(160, 75, P3D);
}

void setup()
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
  opc.ledGrid(0, 64, 30, width/2, height/2, width/64.0, height/30.0, 0, false);
}

void draw()
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

  color alteredColor = selectedColor;

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

void initFilenameArray () {
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

void loadNextClip() {
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

void loadPrevClip() {

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