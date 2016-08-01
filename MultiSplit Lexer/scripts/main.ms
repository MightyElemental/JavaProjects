initGUI() 1600 900 Basic Square;
setGUIMode() game;
showGUI();
setVar() @x number 0;

setVar() @draw function {
  drawRect() @x 100 50 50;
}
setVar() @update function {
  setVar() @x number @x+1;
  sleep() 50;
}
exec() @update;
drawGUI();
goto() 13;