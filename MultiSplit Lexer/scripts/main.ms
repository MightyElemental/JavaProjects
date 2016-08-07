addScript() test;
setVar() @draw function {
  drawRect() @x 50 20 20;
  drawRect() 50 @x 20 20;
  setVar() @x number @x+1;
  addString() @x;
}
exec() @loginFunc;
exec() @setupGUI;
addString() draw |;
drawGUI();
sleep() 100;
println();
clearString();
goto() 10;