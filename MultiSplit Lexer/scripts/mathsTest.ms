setVar() @h number 0;
setVar() @correct number 0;
setVar() @qfunc function {
  setVar() @num1 random 0 9;
  setVar() @num2 random 1 9;
  setVar() @q array @h @num1 + @num2;
  getInput() @ans @q[@h] =;
  setVar() @a array @h @ans;
  setVar() @h number @h+1;
}
setVar() @time1 number getTime();
exec() @qfunc;
exec() @qfunc;
exec() @qfunc;
exec() @qfunc;
exec() @qfunc;
setVar() @time2 number getTime();
setVar() @z number 0;
if() @h > @z 27;
  setVar() @h number @h-1;
  setVar() @n number @q[@h];
  setVar() @an number @a[@h];
  if() @n == @an 25;
    setVar() @correct number @correct+1;
  goto() 19;

setVar() @timeTook number @time2-@time1;
setVar() @timeTook number @timeTook/1000;
addString() It took you @timeTook seconds!;
println();
clearString();
addString() You got @correct \ @a~ correct!;
println();
clearString();
setVar() @perQ number @timeTook/@a;
addString() That is @perQ seconds per question;
println();