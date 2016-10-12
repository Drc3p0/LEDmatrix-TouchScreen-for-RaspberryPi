
int[] buttonRow1y = new int[3];
int[] buttonRow2y = new int[2];
int buttonRow1x;
int buttonRow2x;
int buttonDiameter = 120;
color buttonColor, baseColor;
color buttonHighlight;
color currentColor;
color backgroundColor;

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
  
  void settings() {
    size(800, 480);
    //size(800, 480);  
    fullScreen();
  }
  
  void setup() {
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
  
  void draw() {
    
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
    
    image(prevIcon, buttonRow1x, buttonRow1y[0], buttonDiameter*.6, buttonDiameter*.6);
    image(micIcon, buttonRow1x, buttonRow1y[1], buttonDiameter*.8, buttonDiameter*.8);
    image(nextIcon, buttonRow1x, buttonRow1y[2], buttonDiameter*.6, buttonDiameter*.6);
    image(minusIcon, buttonRow2x, buttonRow2y[0], buttonDiameter*.6, buttonDiameter*.6);
    image(plusIcon, buttonRow2x, buttonRow2y[1], buttonDiameter*.6, buttonDiameter*.6);
    
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
    ellipse(colorWheelX, colorWheelY, colorWheelSize*.31, colorWheelSize*.31);
  }
  
  
  void mouseDownUpdate(int x, int y) {
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
  
  
  void mousePressed(MouseEvent event) {
    int x = event.getX();
    int y = event.getY();
    mouseDownUpdate(x, y);
  }
  
  void mouseReleased(MouseEvent event) {
    prevButtonOver = false;
    nextButtonOver = false;
    decBrightButtonOver = false;
    incBrightButtonOver = false;
  }
  
  boolean mouseOverCircle(int x, int y, int diameter) {
    return pointOverCircle(mouseX, mouseY, x, y, diameter);
  }
  
  boolean pointOverCircle(int pointX, int pointY, int cirX, int cirY, int diameter) {
    float disX = cirX - pointX;
    float disY = cirY - pointY;
    if (sqrt(sq(disX) + sq(disY)) < diameter/2 ) {
      return true;
    } else {
      return false;
    }
  }
  
  color getSelectedColor() {
    return selectedColor;
  }
  
  boolean getMicOn() {
    return micToggleOn;
  }

}