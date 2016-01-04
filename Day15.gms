$ondollar
$title Advent of Code Day 15, Margaret Pearce

$offsymxref offsymlist offuelxref offuellist offupper
option limrow = 0, limcol = 0;
option optcr=0.0001;
option optca=0.0001;

setsi /sprinkles, butterscotch, chocolate, candy/
p /capacity, durability, flavor, texture, calories/
p_sub(p) /capacity, durability, flavor, texture/;

table 
coefficients(i,p)
		capacity	durability	flavor	texture	calories
sprinkles	2		0		-2	0	3
butterscotch	0		5		-3	0	3
chocolate	0		0		5	-1	8	
candy		0		-1		0	5	8;

free variables
totalScore
;

positive variables
totalProperty(p)
;

integer variables
numTeaspoons(i)
;

equation
totalTeaspoons		"Total number of teaspoons must be equal to 100"
defTotalProperty(p)	"Calculate the total amount of property p"
limitedCalories		"Limit calories to 500 (Part B)"
obj			"Total score"
;

totalTeaspoons..
sum(i, numTeaspoons(i)) =e= 100;

defTotalProperty(p)..
totalProperty(p) =e= sum(i, numTeaspoons(i)*coefficients(i,p));

limitedCalories..
totalProperty("calories") =e= 500;

obj..
totalScore =e= prod(p_sub, totalProperty(p_sub));

numTeaspoons.lo(i) = 0;
numTeaspoons.up(i) = 100;

model puzzle /totalTeaspoons, defTotalProperty, obj/;
solve puzzle using minlp maximizing totalScore;

display numTeaspoons.l;
display totalProperty.l;
display totalScore.l;



model puzzlePartB /all/;
solve puzzlePartB using minlp maximizing totalScore;

display numTeaspoons.l;
display totalProperty.l;
display totalScore.l;