setVar() @rand random 0 100;
setVar() @lastRand number @rand;
setVar() @l string l;
setVar() @losses number 0;
setVar() @wins number 0;
addString() Number> @rand;
println();
setVar() @higherOrLower function {
  getInput() @hl Higher(h) or Lower(l)?;
  setVar() @rand random 0 100;
  if() @hl == @l 14;
    exec() @choseLow;
  goto() 16;
    exec() @choseHigh;
  clearString();
  addString() Wins   | @wins;
  println();
  clearString();
  addString() Losses | @losses;
  println();
  clearString();
  addString() Number | @rand;
  println();
  setVar() @lastRand number @rand;
}

setVar() @choseLow function {
  if() @rand > @lastRand 31;
    setVar() @losses number @losses+1;
  break();
    setVar() @wins number @wins+1;
  break();
}

setVar() @choseHigh function {
  if() @rand < @lastRand 39;
    setVar() @losses number @losses+1;
  break();
    setVar() @wins number @wins+1;
  break();
}
exec() @higherOrLower;