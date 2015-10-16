setVar() @master string James;
setVar() @loginFunc function {
getInput() @name Enter your name;
if() @name == @master 8;
addString() Welcome James!!;
println();
halt();
addString() INCORRECT!!!;
println();
clearString();
goto() 3;
}

exec() @loginFunc;