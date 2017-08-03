/************************************************************
*                                                           *
* Square 1 solver                                           *
*                                                           *
* Copyright 2001, by Jaap Scherphuis,  jaapsch@yahoo.com    *
*                                                           *
* This program, including its source code, is free and may  *
* not be used for any commercial purpose whatsoever without *
* the author's permission. It is free to use for any non-   *
* commercial purpose provided this copyright notice remains *
* intact and the users of any program that incorporates     *
* any part of this code are clearly informed of the         *
* authorship of this code.                                  *
*                                                           *
************************************************************/

#include <fstream>
#include "EngineTwst.h"
#include "main.h"

#define DEFAULTDEPTH 99

using namespace std;

#include <iostream>


int main(int argc, char *argv[]){
	// //get instance of search engine
	// Position1 p1;
	// int mode=0;		//bit0 set if all solns of shortest length, bit1 set if middle ignored
	// int depth=DEFAULTDEPTH;
	// ostream* output= NULL;

	// if(argc==1){
	// 	help();
	// 	return 0;
	// }


	// //Read in position from command line
	// char b=0;  // count of non-switch parameters
	// int dptrn, dptwst;
	// char *posstr;
	// for(int a=1; a<argc; a++){
	// 	if(argv[a][0]=='-'){
	// 		if(ReadSwitch( argv[a], mode, depth )) return 1;
	// 	}else if(b==0){
	// 		posstr=argv[a];
	// 		b++;
	// 	}else if(b==1){
	// 		b++;
	// 		if(ReadFileName(argv[a],&output)) return 1;
	// 	}else{
	// 		cerr<<"Error: Too many command line parameters."<<endl;
	// 		return 1;
	// 	}
	// }
	// if(b==0){
	// 	cerr<<"Error: No position or move sequence given."<<endl;
	// 	return 2;
	// }

	// //choose correct engine for the metric
	// Engine* eng;
	// if(mode&4){
	// 	eng= (Engine*) new EngineTwst();
	// }else{
	// 	eng= (Engine*) new EngineTrn();
	// }
	// //initialise its pruning tables
	// cout<<"Initializing...\r"<<flush;
	// eng->Initialise();
	// eng->InitPermTable();
	// cout<<"               \r"<<flush;

	// //parse position/move-sequence now that have the transition tables
	// if(posstr[0]=='/' || posstr[0]=='('){
	// 	if(ReadMoves(posstr,p1,dptrn,dptwst)) return 1;
	// }else{
	// 	if(ReadPosition(posstr,p1)) return 1;
	// 	dptrn=dptwst=DEFAULTDEPTH;
	// }

	// //strip middle layer
	// if(mode&2){
	// 	mode-=2;
	// 	p1.IgnoreMiddle();
	// }

	// // override depth if explicitly set by switch
	// if( depth!=DEFAULTDEPTH) dptrn=dptwst=depth;

	// // set correct search depth
	// if(mode&4){
	// 	mode-=4;
	// 	depth= dptwst;
	// }else{
	// 	depth= dptrn;
	// }

	// eng->DoSearch(p1,depth,mode,output);

	// //Close output file if any.
	// if(output) delete output;
	// delete eng;
	// cout<<"                                                                               \r";
	// return 0;
  return 0;
}



void help(){
	cout<<"Square1 Solver (v3). Written by Jaap Scherphuis <jaap@org2.com>"<<endl;
	cout<<"Usage:"<<endl;
	cout<<"  SQUARE1 [-a] [-m] [-lxx] <position>|<movelist> [filename]"<<endl;
	cout<<"The command line switches are all optional:"<<endl;
	cout<<"  -a  All solutions of the shortest length will be given instead of just one."<<endl;
	cout<<"  -m  The middle layer will be ignored, so solns may have any number of twists."<<endl;
	cout<<"  -l  Used to specify the maximal search depth."<<endl;
	cout<<"  -t  The twist metric is used (i.e. only twists are considered moves)."<<endl;
	cout<<"The pattern to be solved can be specified by a position description or by a"<<endl;
	cout<<"list of moves. A position is given by using the letters A-H for the corners"<<endl;
	cout<<"and 1-8 for the edges. The pieces are listed clockwise, first the top layer"<<endl;
	cout<<"starting from the front left, then the bottom layer starting from the front."<<endl;
	cout<<"This is optionally followed by a - for a square middle layer, or / for a kite"<<endl;
	cout<<"shaped one. The solved position is A1B2C3D45E6F7G8H."<<endl;
	cout<<"A sequence of moves may be given instead, e.g. (1,0)/(-1,0), and the -m switch"<<endl;
	cout<<"can then be used if the middle layer is to be ignored."<<endl;
	cout<<"If a filename is given, then the results are saved to it."<<endl;
}


int ReadPosition(const char* Input, Position1& p1){
	char a,b,c;
	unsigned f=0,g;
	char Output[26];
	Output[24]=' ';Output[25]=0;

	if( strlen(Input)!=16 && strlen(Input)!=17 ){
		cerr<<"Error: Position must be given by a 16 or 17 character string."<<endl;
		return 1;
	}

	b=0;
	for( a=0; a<16 && b<24; a++ ){
		c=Input[a];
		if(c>='1' && c<='8'){
			Output[b++]=c;
			g= (1<<(c-'1'));
		}else if( (c>='a' && c<='h') || (c>='A' && c<='H') ){
			if(b==11){
				ParseError(Input, "Error: Corner piece lies in both layers.", a);
				return 1;
			}
			if(b==5 || b==17){
				ParseError(Input, "Error: Corner piece  blocks the seam.", a);
				return 1;
			}
			c|=32;	//make lowercase
			Output[b++]=c-'a'+'A';
			Output[b++]=c-'a'+'A';
			g= (1<<(c-'a'+8));
		}else{
			ParseError(Input, "Error: Position contains an incorrect character.", a);
			return 1;
		}
		if(f&g){
			ParseError(Input, "Error: Position contains two identical pieces.", a);
			return 1;
		}
		f|=g;
	}

	if(Input[16]){
		if(Input[16]=='/' || Input[16]=='-' ){
			Output[24]=Input[16];
		}else{
			ParseError(Input, "Error: If the middle layer shape is specified, it must be / or -.", 16);
			return 1;
		}
	}else{
		Output[24]=' ';
	}
	p1.Initialise(Output);
	return 0;
}



int ReadSwitch(const char* sw, int& mode, int& depth){
	unsigned char a,d=0;
	if(sw[1]=='l' || sw[1]=='L'){
		a=2;
		while(sw[a] && a<4){
			if(sw[a]>='0' && sw[a]<='9'){
				d=d*10+sw[a]-'0';
			}else{
				cerr<<"Error: There can only be a number after the -l switch."<<endl;
				return 1;
			}
			a++;
		}
		if(sw[a]){
			cerr<<"Error: There can only be a number <100 after the -l switch."<<endl;
			return 1;
		}
	}else if(sw[1]=='T' || sw[1]=='t'){
		mode|=4;
	}else if(sw[1]=='M' || sw[1]=='m'){
		mode|=2;
	}else if(sw[1]=='A' || sw[1]=='a'){
		mode|=1;
	}else{
		cerr<<"Error: Unrecognised command line switch -"<<sw[1]<<"."<<endl;
		return 1;
	}
	if(d) depth=d;
	return 0;
}







int ReadFileName(const char* fn, ostream** os){
	ostream* o=new ofstream(fn);
	if (!(*o)){
		cerr<<"Error: Could not open output file "<<fn<<"."<<endl;
		return 1;
	}
	*os=o;
	return 0;
}



int ReadMoves(const char* Input, Position1& p1, int& dptrn, int& dptwst){
	p1.Initialise("AA1BB2CC3DD45EE6FF7GG8HH-");	//starting position

	int md=0;
	//  When parsing .. ) / ( - 2 , - 2 1 )  the md variable
	// has values        0 0 6 5 4 3 2 2 1 0

	int a=strlen(Input)-1;    //parse starting from the end
	int tp=0, bt=0, v=1;
	char c;
	dptrn=dptwst=0;
	while(a>=0){
		c=Input[a];
		switch(md){
		case 0:
			if(c=='/') {
				p1.Twist();
				dptrn++; dptwst++;
			}else if(c==')') md=1;
			else {
				ParseError(Input, "Syntax error: Only a / can occur outside brackets.", a);
				return 1;
			}
			break;
		case 1:
			if(c<'0' || c>'9') {
				ParseError(Input, "Syntax error: Number expected before ).", a);
				return 1;
			}
			//fallthrough intentional
		case 2:
			if(c>='0' && c<='9') { md=2; bt+=v*(c-'0'); v*=10; break;}
			else if(c=='-'){ md=3; bt=-bt; break;}
			else if(c!=','){
				ParseError(Input, "Syntax error: Before this number, a minus or a comma is expected.", a);
				return 1;
			}
			//fallthrough intentional
		case 3:
			if(c==','){ md=4; v=1; break;}
			else{
				ParseError(Input, "Syntax error: Comma expected before this number.", a);
				return 1;
			}
		case 4:
			if(c<'0' || c>'9'){
				ParseError(Input, "Syntax error: Number expected before a comma.", a);
				return 1;
			}
			//fallthrough intentional
		case 5:
			if(c>='0' && c<='9') { md=5; tp+=v*(c-'0'); v*=10; break;}
			else if(c=='-'){ md=6; tp=-tp; break;}
			else if(c!='('){
				ParseError(Input, "Syntax error: Before this number, a minus or left-bracket is expected.", a);
				return 1;
			}
			//fallthrough intentional
		case 6:
			if(c=='('){
				tp=(-tp)%12; bt=(-bt)%12;
				if(tp<0) tp+=12;
				if(bt<0) bt+=12;
				if(tp) dptrn++;
				if(bt) dptrn++;

				while( tp>0) tp-=p1.Top();
				while( bt>0) bt-=p1.Bottom();
				if(tp+bt !=0){
					ParseError(Input, "Shape error: The seam is blocked by a corner.", a);
					return 1;
				}
				md=0; v=1; bt=tp=0;
			}
			else{
				ParseError(Input, "Syntax error: Expected left-bracket before this number.", a);
				return 1;
			}
			break;
		}
		a--;
	}
	return 0;
}


void ParseError(const char* Input, const char* Err, char pos){
	char buf[71];buf[70]=0;
	cerr<<Err<<endl;
	char c;
	while(Input){
		strncpy(buf, Input, 70);
		cerr<<"       "<<buf<<endl;
		if(pos<70){
			for(c=0;c<70;c++) buf[c]=' ';
			buf[pos]='^';
			cerr<<"       "<<buf<<endl;
		}
		pos-=70;
		if(strlen(Input)>70) Input+=70;
		else Input=NULL;
	}
}
