addScript() login;
exec() @loginFunc;
setVar() @r random 1 100;
setVar() @a number 0;
addString() Guess a number between 1 and 100!;
println();
clearString();
setVar() @e function {
  clearString();
  addString() You guessed in @a attempt(s)!;
  println();
  halt();
}
getInput() @g;
setVar() @a number @a+1;
setVar() @gn number @g;
if() @gn > @r 20;
  addString() Number too high!;
goto() 26;
  if() @gn < @r 23;
    addString() Number too low!;
  goto() 26;
    addString() You guessed correctly! The number was @r;
    println();
  exec() @e;
println();
clearString();
goto() 14;