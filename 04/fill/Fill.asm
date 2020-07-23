// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed.
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

 M=0 // DONT KNOW IF THIS ACTUALLY DOES SOMETHHING

(LOOP)
 @KBD // keyboard value - check slide to see if thats right shortening
 D=M // stores at D

 @WHITE
 D;JEQ // if keyboard is not pressed, value is 0  go to WHITE
 @BLACK
 0;JMP // if keyword is pressed , go to BLACK

(WHITE)
 D=M // put place (is place the equivalent to a curr ASK) into D
 @LOOP
 D;JLT // jump to LOOP

 D=M
 @SCREEN
 A=A+D // gets place in the screen
 M=0 // fill with white

 M=M-1 // lower place by 1
 @LOOP
 0;JMP // jump to LOOP

(BLACK)

 D=M
 @8192 // max that place can be I THINK
 D=D-A
 @LOOP
 D;JGE // jump to LOOP

 D=M
 @SCREEN
 A=A+D // calculate place
 M=-1 // fill black

 M=M+1 // add to place by 1
 @LOOP
 0;JMP //go back to LOOP

(END)
 @END
 0;JMP // Infinite loop at end
