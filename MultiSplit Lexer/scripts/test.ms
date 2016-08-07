setVar() @master string James;
setVar() @loginFunc function {
getInput() @name Enter your name;
if() @name == @master 8;
  addString() Welcome James!!;
  println();
break();
  addString() INCORRECT!!!;
  println();
  clearString();
goto() 3;
}
setVar() @setupGUI function {
  setVar() @x number 1;
  getInput() @title Enter GUI Name;
  initGUI() 1600 900 @title;
  setGUIMode() game;
  showGUI();
}
setVar() @draw function {
  drawRect() 20 20 @x 50;
  setVar() @x number @x+1;
}