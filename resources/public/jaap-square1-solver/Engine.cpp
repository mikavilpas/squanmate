#include "Engine.h"
using namespace std;

#include <iostream>
#include <string>
#include <sstream>


//Global variables
	// Pruning/transition tables
	HalfLayer hl[13];			//the 13 possible half layers
	Layer layers[13][13];		//the 13^2 theoretically possible layers made of two halves
								//  It also holds transition data for layer turns
	char ShapeTable[13][13][13][13][2];		//pruning table; gods algorithm for shape+parity
								//It holds length of position till even parity cube.
								//the high bit is set if a twist changes parity.
	char PermTable[2][40320];		//pruning table; #twists to solve corner|edge permutation
	unsigned PermTwistTable[40320];		//transition table for twists
	unsigned PermTopTable[40320];		//transition table for top layer turns
	unsigned PermBotTable[40320];		//transition table for bottom layer turns

/***************************************************
*           Initialization functions               *
***************************************************/


void Engine::InitLayers(){

	int a,b,c,d,t;

	//Construct the 13 possible half layers
	hl[0].Set("EEEEEE","12345");

	hl[1].Set( "CcEEEE", "1234");
	hl[2].Set( "ECcEEE", "1235");
	hl[3].Set( "EECcEE", "1245");
	hl[4].Set( "EEECcE", "1345");
	hl[5].Set( "EEEECc", "2345");

	hl[6].Set( "CcCcEE", "124");
	hl[7].Set( "CcECcE", "134");
	hl[8].Set( "CcEECc", "234");
	hl[9].Set( "ECcCcE", "135");
	hl[10].Set("ECcECc", "235");
	hl[11].Set("EECcCc", "245");

	hl[12].Set("CcCcCc", "24");

	//combine halves to make the 13*13 possible layers
	for(a=0;a<13;a++){
		for(b=0;b<13;b++){
			layers[a][b].Set(hl[a],hl[b]);
		}
	}

	//For each layer, find next layer shape when turned clockwise,
	// where NextLeft and NextRight are the two new halves.
	char nm[13]; nm[12]=0;
	//now construct transition tables.
	for(a=0;a<13;a++){
		for(b=0;b<13;b++){
			//Construct name of turned layer
			t=layers[a][b].Turn;
			for(c=0; c<t; c++) nm[c]=layers[a][b].Name[c+12-t];
			for(c=t; c<12;c++) nm[c]=layers[a][b].Name[c   -t];
			//find turned layer in layerlist
			for(c=0;c<13;c++){
				for(d=0;d<13;d++){
					if(layers[c][d].Equals(nm)){
						layers[a][b].NextLeft=c;
						layers[a][b].NextRight=d;
						c=13; break;
					}
				}
			}
		}
	}

	// set twist parity changes
	for(a=0;a<13;a++)for(b=0;b<13;b++)for(c=0;c<13;c++)for(d=0;d<13;d++){
		ShapeTable[a][b][c][d][0] = ShapeTable[a][b][c][d][1] = 0;
	}
	for(b=0;b<13;b++){
		if(hl[b].Pieces&1){
			for(c=0;c<13;c++){
				if(hl[c].Pieces&1){
					for(a=0;a<13;a++)for(d=0;d<13;d++){
						ShapeTable[a][b][c][d][0] = ShapeTable[a][b][c][d][1] = (char)128;
					}
				}
			}
		}
	}
}



/***************************************************
*            Search algorithm functions            *
***************************************************/

int Engine::DoSearch(Position1& p1, int dp, int md, ostream* os){
	InitPos=p1;
	MaxDepth=dp;
	Mode=md;
	OutStrm=os;
	// cout<<"Position: "<<InitPos.Strng()<<endl;
	return StartPhase1();
}



/***************************************************
*       Output routine                             *
***************************************************/

void Engine::FoundSol(int l, bool large){
  solutionsFound++;
  // cout<<"found solution! now have this many solutions: " << solutionsFound;
  // if(MaxDepth>l || Mode==0){
  //   cout<<"Length:"<<l<<endl;
  //  }
  if(Mode)	MaxDepth=l;
	else		MaxDepth=large? l-2: l-1;

   ostringstream os;
   moves.print(os);
   solution = os.str();

	moves.print(cout);
	if(OutStrm) moves.print(*OutStrm);
}
