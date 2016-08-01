setVar() @master string James;
setVar() @loginFunc function {
getInput() @name Enter your name;
if() @name == @master 8;
  addString() Welcome James!!;
  println();
goto() 17;
  addString() INCORRECT!!!;
  println();
  clearString();
goto() 3;
}

exec() @loginFunc;


setVar() @num number 5;
clearString();
setVar() @num number @num*@num;
addString() @num;
println();
goto() 18;