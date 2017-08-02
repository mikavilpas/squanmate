#include "EngineTrn.h"

using namespace std;

#include <iostream>

/***************************************************
*          Initialization functions                *
***************************************************/

void EngineTrn::InitShapeTable(){
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
			if((ShapeTable[a][b][c][d][e]&127)==l){
				//try twist
				e2= (ShapeTable[a][b][c][d][e]&128) ? 1-e : e ;
				if( (ShapeTable[a][c][b][d][e2]&127) == 0){
					i++;
					ShapeTable[a][c][b][d][e2]+=l+1;
				}

				//try turning top layer
				a2=a;b2=b;e2=e;
				do{
					e2= (layers[a2][b2].TurnParity) ? 1-e2 : e2 ;
					t=a2;
					a2=layers[a2][b2].NextLeft;
					b2=layers[t ][b2].NextRight;

					if( (ShapeTable[a2][b2][c][d][e2]&127) == 0){
						i++;
						ShapeTable[a2][b2][c][d][e2]+=l+1;
					}
				}while(a2!=a || b2!=b);


				//try turning bottom layer
				c2=c;d2=d;e2=e;
				do{
					e2= (layers[c2][d2].TurnParity) ? 1-e2 : e2 ;
					t=c2;
					c2=layers[c2][d2].NextLeft;
					d2=layers[t ][d2].NextRight;

					if( (ShapeTable[a][b][c2][d2][e2]&127) == 0){
						i++;
						ShapeTable[a][b][c2][d2][e2]+=l+1;
					}
				}while(c2!=c || d2!=d);

			}
		}
	}while(i);
}


void EngineTrn::InitPermTable(){
	unsigned int a,b,i;
	char t,c,l;
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
	PermTable[0][0]=1;
	l=0;
	do{
		l++; i=0;
		for(a=0;a<40320;a++) for(t=0;t<2;t++){
			if(PermTable[t][a]==l){
				//try twist
				b= PermTwistTable[a];
				if( PermTable[1-t][b] == 0 ){
					i++;
					PermTable[1-t][b]=l+1;
				}

				//try turning top layer
				b=a;
				for(c=0;c<4;c++){
					b= PermTopTable[b];
					if( PermTable[t][b] == 0 ){
						i++;
						PermTable[t][b]=l+1;
					}
				}

				//try turning bottom layer
				b=a;
				for(c=0;c<4;c++){
					b= PermBotTable[b];
					if( PermTable[t][b] == 0 ){
						i++;
						PermTable[t][b]=l+1;
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
int EngineTrn::StartPhase1(){
	//first extract the starting shape
	Position1 p1=InitPos;
	//Perform phase 1 search, for increasing depths
	Nodes2=Nodes1=0;
	Nodes1Low=0;

	// Treat empty phase1 separately; phase1 is empty or ends in a twist.
	Length1=0;
	if(p1.Depth()==0) StartPhase2(p1,-1);	
	for(Length1=1;  Length1<=MaxDepth; Length1++)
		Phase1(p1, Length1, -1);
	return 0;
}


//Perform phase 1 search on given position
//l1=#moves still to be done, lm=last move done
int EngineTrn::Phase1(Position1 Ps1, char l1, char lm){
	char c;
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
			//Found a solution to phase 1; Only do it if ended on a twist
			if(lm==0) return StartPhase2(Ps1,lm);
			else return 0;
		}else if( l1<4 ) return 0;	//return if too few moves to escape cube shape
	}


	//try each possible move. First twist;
	if(lm!=0){
		Ps1.Twist();
		moves.push(0);
		r+=Phase1(Ps1,l1-1,0);
		if(r && Mode==0) return r;
		moves.pull();
		Ps1.Twist();
	}

	//Try top layer
	if(lm!=1 && lm!=2){
		c=0;
		do{
			c+= Ps1.Top();
			if(c>=12) break;
			moves.push(c);
			r+=Phase1(Ps1,l1-1,1);
			if(r && Mode==0) return r;
			moves.pull();
		}while(true);
	}

	//Try bottom layer
	if(lm!=2){
		c=0;
		do{
			c+= Ps1.Bottom();
			if(c>=12) break;
			moves.push(-c);
			r+=Phase1(Ps1,l1-1,2);
			if(r && Mode==0) return r;
			moves.pull();
		}while(true);
	}

	return r;
}

/***************************************************
*       Phase 2 Search algorithm functions         *
***************************************************/


//control a phase 2 search
int EngineTrn::StartPhase2(Position1 Ps1, char lm){

	//first extract the starting position
	Position2 p2;
	p2.Initialise(Ps1);

	//Perform phase 2 search, for increasing depths
	int r;
	for(r=0, Length2=0; Length2<=MaxDepth-Length1 && r==0; Length2++)
		r=Phase2(p2, Length2, lm);
	if(r)
		if(MaxDepth>Length1 || (MaxDepth==Length1 && Mode==0))
			r=0;	//if phase2 is still significant, then must continue search
	return r;
}


//Perform phase 2 search on given position of given depth
int EngineTrn::Phase2(Position2 Ps2, char l2, char lm){
	char c;
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
		//Found a solution to phase 2;
		if( Ps2.IsSolved() ){
			FoundSol( moves.length(), false );
			return 1;
		}else return 0;
	}

	//try each possible move. First twist;
	if(lm!=0){
		if(Ps2.CanTwist()){
			Ps2.Twist();
			moves.push(0);
			r+=Phase2(Ps2,l2-1,0);
			moves.pull();
			if(r && Mode==0) return r;
			Ps2.Twist();
		}
	}

	//Try top layer
	if(lm!=1 && lm!=2){
		c=0;
		do{
			c+= Ps2.Top();
			if(c>=12) break;
			moves.push(c);
			r+=Phase2(Ps2,l2-1,1);
			moves.pull();
			if(r && Mode==0) return r;
		}while(true);
	}

	//Try bottom layer
	if(lm!=2){
		c=0;
		do{
			c+= Ps2.Bottom();
			if(c>=12) break;
			moves.push(-c);
			r+=Phase2(Ps2,l2-1,2);
			moves.pull();
			if(r && Mode==0) return r;
		}while(true);
	}

	return r;
}
