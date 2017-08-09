#include "EngineTwst.h"

using namespace std;

#include <iostream>

/***************************************************
*          Initialization functions                *
***************************************************/

void EngineTwst::InitShapeTable(){
	int a,b,c,d,e, i, a2,b2,c2,d2,e2, t;
	char l;

	//build pruning table
	ShapeTable[7 ][7 ][10][10][0]|=1;
	ShapeTable[10][10][7 ][7 ][0]|=1;
	ShapeTable[7 ][7 ][7 ][7 ][1]|=1;
	ShapeTable[10][10][10][10][1]|=1;
	
	l=0;
	do{
		l++; i=0;
		for(a=0;a<13;a++)for(b=0;b<13;b++)for(c=0;c<13;c++)for(d=0;d<13;d++)for(e=0;e<2;e++){
			if((ShapeTable[a][c][b][d][e]&127)==l){

				// do twist and then any layer turns; considered a single move
				//try twist
				e2= (ShapeTable[a][c][b][d][e]&128) ? 1-e : e ;
				if( (ShapeTable[a][b][c][d][e2]&127) == 0){
					i++;
					ShapeTable[a][b][c][d][e2]+=l+1;
					//try turning top layer
					a2=a;b2=b;c2=c;d2=d;
					do{
						e2= (layers[a2][b2].TurnParity) ? 1-e2 : e2 ;
						t=a2;
						a2=layers[a2][b2].NextLeft;
						b2=layers[t ][b2].NextRight;
						if( (ShapeTable[a2][b2][c2][d2][e2]&127) == 0){
							ShapeTable[a2][b2][c2][d2][e2]+=l+1;
							i++;
						}

						//try turning bottom layer
						do{
							e2= (layers[c2][d2].TurnParity) ? 1-e2 : e2 ;
							t=c2;
							c2=layers[c2][d2].NextLeft;
							d2=layers[t ][d2].NextRight;
							if( (ShapeTable[a2][b2][c2][d2][e2]&127) == 0){
								ShapeTable[a2][b2][c2][d2][e2]+=l+1;
								i++;
							}
						}while(c2!=c || d2!=d);

					}while(a2!=a || b2!=b);
				}
			}
		}
	}while(i);
}

void EngineTwst::InitPermTable(){
	unsigned int a,b,i;
	char t,c,d,l;
	char Pos[9];

	//clear perm tables
	for(a=0;a<40320;a++){
		PermTable[0][a]=PermTable[1][a]=0;
		PermTwistTable[a]=0;
		PermBotTable[a]=0;
		PermTopTable[a]=0;
	}

	//build transition tables
	for(a=0;a<40320;a++){
		//twist
		Num2Perm(Pos, 'A', a, 8);
		t=Pos[2];Pos[2]=Pos[4]; Pos[4]=t;
		t=Pos[3];Pos[3]=Pos[5]; Pos[5]=t;
		PermTwistTable[a]=Perm2Num(Pos,8);
	
		//top layer turn
		Num2Perm(Pos, 'A', a, 8);
		t=Pos[3]; Pos[3]=Pos[2]; Pos[2]=Pos[1]; Pos[1]=Pos[0]; Pos[0]=t;
		PermTopTable[a]=Perm2Num(Pos,8);

		//bottom layer turn
		Num2Perm(Pos, 'A', a, 8);
		t=Pos[7]; Pos[7]=Pos[6]; Pos[6]=Pos[5]; Pos[5]=Pos[4]; Pos[4]=t;
		PermBotTable[a]=Perm2Num(Pos,8);
	}

	//build perm table

	//set all rotations from start of top/bot layers to 1 (=depth 0)
	b=0;
	for(c=0;c<4;c++){
		PermTable[0][b]=1;
		for(d=0;d<4;d++){
			PermTable[0][b]=1;
			b= PermBotTable[b];
		}
		b= PermTopTable[b];
	}

	l=0;
	do{
		l++; i=0;
		for(a=0;a<40320;a++) for(t=0;t<2;t++){
			if(PermTable[1-t][a]==l){
				//try twist
				b= PermTwistTable[a];
				if( PermTable[t][b] == 0 ){

					//try turning top layer
					for(c=0;c<4;c++){
						if( PermTable[t][b] == 0 ){
							i++;
							PermTable[t][b]=l+1;
						}
						//try turning bottom layer
						for(d=0;d<4;d++){
							if( PermTable[t][b] == 0 ){
								i++;
								PermTable[t][b]=l+1;
							}
							b= PermBotTable[b];
						}
						b= PermTopTable[b];
					}
				}
			}
		}
	}while(i);
}


/***************************************************
*       Phase 1 Search algorithm functions         *
***************************************************/

//control a phase 1 search
int EngineTwst::StartPhase1(){
  // cout<<"starting phase 1";
	//first extract the starting shape
	Position1 p1=InitPos;
	//Perform phase 1 search, for increasing depths
	Nodes2=Nodes1=0;
	Nodes1Low=0;

	for(Length1=0;  Length1<=MaxDepth && ! solutionsFound; Length1++){
		if( Length1==MaxDepth && (
				(p1.GetMiddle()==-1 && (Length1&1)==0) ||
				(p1.GetMiddle()==1 &&  (Length1&1)!=0) ) ){
			break;
		}
		Phase1(p1, Length1, -1);
	}
	return 0;
}
//Perform phase 1 search on given position
//l1=#moves still to be done, lm<0 on first move, >0 if must now be last move, clear otherwise
int EngineTwst::Phase1(Position1 Ps1, char l1, char lm){
  // cout<<"starting phase 1 worker";
	char b,t;
	int r=0;
	// prune 
	char l=Ps1.Depth();
	if( l > l1) return 0;

	Nodes1Low++;
	if( Nodes1Low>=100000 ){
		Nodes1Low=0;
		Nodes1++;
		cout<<"Len1: "<<(int)Length1<<"  Nodes1:"<<Nodes1;
		if(Nodes1) cout<<"00000";
		cout<<"  Len2: "<<(int)(MaxDepth-Length1)<<"  Nodes2:"<<Nodes2<<"    \r"<<flush;
	}

	if(l==0){
		if(l1==0){
			//Found a solution to phase 1
			return StartPhase2(Ps1,lm);
		}else if( l1<2 ) return 0;	//return if too few moves to escape cube shape
	}
	if( lm>0 ){		//go back if just did a move with b>=6, and not yet solved.
		return 0;
	}

	//Try top layer
	t=0;
	do{
    if (solutionsFound) {
      return 1;
    }

		if(t) moves.push(t);

		//Try bottom layer
		b=0;
		do{
      if (solutionsFound) {
        return 1;
      }

			if(t || b || lm<0 ){
				if(b) moves.push(-b);
				//Do twist;
				Ps1.Twist();
				moves.push(0);
				r+=Phase1(Ps1,l1-1,(b>=6)?1:0);
				moves.pull();
				Ps1.Twist();
				if(b) moves.pull();
				if(r && Mode==0){
					if(t) moves.pull();
					return r;
				}
			}
			b+= Ps1.Bottom();
		}while(b<12 && ! solutionsFound);
		
		if(t) moves.pull();
		t+= Ps1.Top();
	}while(t<12 && ! solutionsFound);

	return r;
}

/***************************************************
*       Phase 2 Search algorithm functions         *
***************************************************/

//control a phase 2 search
int EngineTwst::StartPhase2(Position1 Ps1, char lm){
  // cout<<"starting phase 2";
	int r=0;
	Position2 p2;

	//first extract the starting position
	p2.Initialise(Ps1);

	// set initial phase 2 search depth
	if( Ps1.GetMiddle()<0 ){
		Length2=1;
	}else{
		Length2=0;
	}

	//Perform phase 2 search, for increasing depths
	while(( Length2<=MaxDepth-Length1 && r==0 ) && ! solutionsFound){
		r=Phase2(p2, Length2, lm);
		if( Ps1.GetMiddle() ) Length2+=2;
		else Length2++;
	}

	if(r)
		if((MaxDepth>Length1 || (MaxDepth==Length1 && Mode==0)))
			r=0;	//if phase2 is still significant, then must continue search
	return r;
}

//Perform phase 2 search on given position of given depth
int EngineTwst::Phase2(Position2 Ps2, char l2, char lm){
  if (solutionsFound)
    {
      cout<<"solution found but program still going";
      int a = 3;
      exit(0); // debugger is broken
      return 1;
    }

	char b,t;
	int r=0;
	// prune
	char l =Ps2.Depth();
	if( l > l2) return 0;

	Nodes2++;
	if( (Nodes2&65535)==0 ){
		cout<<"Len1: "<<(int)Length1<<"  Nodes1:"<<Nodes1;
		if(Nodes1) cout<<"00000";
		cout<<"  Len2: "<<(int)(MaxDepth-Length1)<<"  Nodes2:"<<Nodes2<<"    \r"<<flush;
	}

	if(l2==0 && l==0){
		//Found a possible solution to phase 2; still need to turn top/bottom

		//Try top layer
		t=0;
		do{
      if (solutionsFound) {
        return 1;
      }
      if(t) moves.push(t);
		
			//Try bottom layer
			b=0;
			do{
				if(b) moves.push(-b);
				if( Ps2.IsSolved() ){
					FoundSol( moves.lengthtwist(), Ps2.GetMiddle()!=0 );
					if(b) moves.pull();
					if(t) moves.pull();
					return 1;
				}
				if(b) moves.pull();
				b+= Ps2.Bottom();
			}while(b<12 && ! solutionsFound);
		
			if(t) moves.pull();
			t+= Ps2.Top();
		}while(t<12 && ! solutionsFound);
		return 0;
	}else if(lm>0){		//go back if just did a move with b>=6, and not yet solved.
		return 0;
	}

  if (solutionsFound) {
    return 1;
  }

	//Try top layer
	t=0;
	do{
    if (solutionsFound) {
      return 1;
    }
		if(t) moves.push(t);
		
		//Try bottom layer
		b=0;
		do{
      if (solutionsFound) {
        return 1;
      }

			if(t || b || lm){
				if(Ps2.CanTwist()){
					if(b) moves.push(-b);
					//Do twist;
					Ps2.Twist();
					moves.push(0);
					r+=Phase2(Ps2,l2-1,(b>=6)?1:0);
					moves.pull();
					Ps2.Twist();
					if(b) moves.pull();
					if(r && Mode==0){
						if(t) moves.pull();
						return r;
					}
				}
			}
			b+= Ps2.Bottom();
		}while(b<12);

		if(t) moves.pull();
		t+= Ps2.Top();
	}while(t<12);

	return r;
}
