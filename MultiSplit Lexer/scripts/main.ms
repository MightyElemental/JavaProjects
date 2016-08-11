setVar() @time1 number getTime();
setVar() @h number 0;
setVar() @question function {
  setVar() @num1 random 0 9;
  setVar() @num2 random 1 9;
  setVar() @a array @h @num1 + @num2;
  <COM>TOO LITTERAL E.G. 5+4=54
  setVar() @number number @a[@h];
  addString() @a[@h] = @number;
  println();
  clearString();
  setVar() @h number @h+1;
}
exec() @question;
exec() @question;
exec() @question;
exec() @question;