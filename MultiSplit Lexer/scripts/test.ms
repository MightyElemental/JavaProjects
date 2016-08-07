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
  initGUI() 1600 900 Rekt;
  setGUIMode() game;
  showGUI();
}