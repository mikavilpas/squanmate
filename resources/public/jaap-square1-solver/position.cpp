#include "perm.h"
#include "position.h"

static char findhl(char* shp){
  char a;
  for(a=0;a<13;a++){
    if( strncmp( hl[a].Name, shp, 6)==0) break;
	}
	return a;
}

//convert string position to a position1 shape object.
void Position1::Initialise(char inipos[]){
	char shape[25], prm[17], a,b;
	pos[24]=shape[24]=prm[16]=0;
	strncpy(pos,inipos,24);
	b=0;
	for(a=0;a<24;a++){
		prm[b++]=pos[a];
		if(pos[a]>='1' && pos[a]<='8') shape[a]='E';
		else { shape[a++]='C'; shape[a]='c'; }
	}
	
	//get half layer shapes
	t1=findhl(shape);
	t2=findhl(shape+6);
	b1=findhl(shape+12);
	b2=findhl(shape+18);

	//get middle layer
	ml= (inipos[24]=='-')? 1 : (inipos[24]=='/')? -1 : 0;

	//get permutation parity by counting # of inversions.
	pm=0;
	for(a=0;a<16;a++){
		for(b=a+1;b<16;b++){
			if(prm[a]>prm[b]) pm^=1;
		}
	}
}

//top layer turn
char Position1::Top(){
	char c,d;
	pm= (layers[t1][t2].TurnParity) ? 1-pm : pm ;
	d=layers[t1][t2].Turn;
	c=t1;
	t1 =layers[t1][t2].NextLeft;
	t2=layers[c ][t2].NextRight;
	TurnLayer(pos,d);
	return d;
}

//bottom layer turn
char Position1::Bottom(){
	char c,d;
	pm= (layers[b1][b2].TurnParity) ? 1-pm : pm ;
	d=layers[b1][b2].Turn;
	c=b1;
	b1=layers[b1][b2].NextLeft;
	b2=layers[c ][b2].NextRight;
	TurnLayer(pos+12,d);
	return d;
}

//twist of rhs
void Position1::Twist(){
	char b;
	pm= (ShapeTable[t1][t2][b1][b2][0]&128) ? 1-pm : pm ;
	b=t2;t2=b1;b1=b;ml=-ml;
	TwistLayers();
}



void Position1::TurnLayer(char* ps, char d){
	char c;
	switch(d){
	case 1:
		     c=ps[11]; ps[11]=ps[10]; ps[10]=ps[ 9]; ps[ 9]=ps[ 8];
		ps[ 8]=ps[ 7]; ps[ 7]=ps[ 6]; ps[ 6]=ps[ 5]; ps[ 5]=ps[ 4];
		ps[ 4]=ps[ 3]; ps[ 3]=ps[ 2]; ps[ 2]=ps[ 1]; ps[ 1]=ps[ 0]; ps[ 0]=c;
		break;
	case 2:
		c=ps[11]; ps[11]=ps[ 9]; ps[ 9]=ps[ 7]; ps[ 7]=ps[ 5]; ps[ 5]=ps[ 3]; ps[ 3]=ps[ 1]; ps[ 1]=c;
		c=ps[10]; ps[10]=ps[ 8]; ps[ 8]=ps[ 6]; ps[ 6]=ps[ 4]; ps[ 4]=ps[ 2]; ps[ 2]=ps[ 0]; ps[ 0]=c;
		break;
	case 3:
		c=ps[11]; ps[11]=ps[ 8]; ps[ 8]=ps[ 5]; ps[ 5]=ps[ 2]; ps[ 2]=c;
		c=ps[10]; ps[10]=ps[ 7]; ps[ 7]=ps[ 4]; ps[ 4]=ps[ 1]; ps[ 1]=c;
		c=ps[ 9]; ps[ 9]=ps[ 6]; ps[ 6]=ps[ 3]; ps[ 3]=ps[ 0]; ps[ 0]=c;
		break;
	case 4:
		c=ps[11]; ps[11]=ps[ 7]; ps[ 7]=ps[ 3]; ps[ 3]=c;
		c=ps[10]; ps[10]=ps[ 6]; ps[ 6]=ps[ 2]; ps[ 2]=c;
		c=ps[ 9]; ps[ 9]=ps[ 5]; ps[ 5]=ps[ 1]; ps[ 1]=c;
		c=ps[ 8]; ps[ 8]=ps[ 4]; ps[ 4]=ps[ 0]; ps[ 0]=c;
		break;
	case 5:
		     c=ps[11]; ps[11]=ps[ 6]; ps[ 6]=ps[ 1]; ps[ 1]=ps[ 8];
		ps[ 8]=ps[ 3]; ps[ 3]=ps[10]; ps[10]=ps[ 5]; ps[ 5]=ps[ 0];
		ps[ 0]=ps[ 7]; ps[ 7]=ps[ 2]; ps[ 2]=ps[ 9]; ps[ 9]=ps[ 4]; ps[ 4]=c;
		break;
	case 6:
		c=ps[11]; ps[11]=ps[ 5]; ps[ 5]=c;
		c=ps[10]; ps[10]=ps[ 4]; ps[ 4]=c;
		c=ps[ 9]; ps[ 9]=ps[ 3]; ps[ 3]=c;
		c=ps[ 8]; ps[ 8]=ps[ 2]; ps[ 2]=c;
		c=ps[ 7]; ps[ 7]=ps[ 1]; ps[ 1]=c;
		c=ps[ 6]; ps[ 6]=ps[ 0]; ps[ 0]=c;
		break;
	case 7:
		     c=ps[11]; ps[11]=ps[ 4]; ps[ 4]=ps[ 9]; ps[ 9]=ps[ 2];
		ps[ 2]=ps[ 7]; ps[ 7]=ps[ 0]; ps[ 0]=ps[ 5]; ps[ 5]=ps[10];
		ps[10]=ps[ 3]; ps[ 3]=ps[ 8]; ps[ 8]=ps[ 1]; ps[ 1]=ps[ 6]; ps[ 6]=c;
		break;
	case 8:
		c=ps[11]; ps[11]=ps[ 3]; ps[ 3]=ps[ 7]; ps[ 7]=c;
		c=ps[10]; ps[10]=ps[ 2]; ps[ 2]=ps[ 6]; ps[ 6]=c;
		c=ps[ 9]; ps[ 9]=ps[ 1]; ps[ 1]=ps[ 5]; ps[ 5]=c;
		c=ps[ 8]; ps[ 8]=ps[ 0]; ps[ 0]=ps[ 4]; ps[ 4]=c;
		break;
	case 9:
		c=ps[11]; ps[11]=ps[ 2]; ps[ 2]=ps[ 5]; ps[ 5]=ps[ 8]; ps[ 8]=c;
		c=ps[10]; ps[10]=ps[ 1]; ps[ 1]=ps[ 4]; ps[ 4]=ps[ 7]; ps[ 7]=c;
		c=ps[ 9]; ps[ 9]=ps[ 0]; ps[ 0]=ps[ 3]; ps[ 3]=ps[ 6]; ps[ 6]=c;
		break;
	case 10:
		c=ps[11]; ps[11]=ps[ 1]; ps[ 1]=ps[ 3]; ps[ 3]=ps[ 5]; ps[ 5]=ps[ 7]; ps[ 7]=ps[ 9]; ps[ 9]=c;
		c=ps[10]; ps[10]=ps[ 0]; ps[ 0]=ps[ 2]; ps[ 2]=ps[ 4]; ps[ 4]=ps[ 6]; ps[ 6]=ps[ 8]; ps[ 8]=c;
		break;
	case 11:
		     c=ps[ 0]; ps[ 0]=ps[ 1]; ps[ 1]=ps[ 2]; ps[ 2]=ps[ 3];
		ps[ 3]=ps[ 4]; ps[ 4]=ps[ 5]; ps[ 5]=ps[ 6]; ps[ 6]=ps[ 7];
		ps[ 7]=ps[ 8]; ps[ 8]=ps[ 9]; ps[ 9]=ps[10]; ps[10]=ps[11]; ps[11]=c;
		break;
	}
}

void Position1::TwistLayers(){
	char c;
	c=pos[11]; pos[11]=pos[17]; pos[17]=c;
	c=pos[10]; pos[10]=pos[16]; pos[16]=c;
	c=pos[ 9]; pos[ 9]=pos[15]; pos[15]=c;
	c=pos[ 8]; pos[ 8]=pos[14]; pos[14]=c;
	c=pos[ 7]; pos[ 7]=pos[13]; pos[13]=c;
	c=pos[ 6]; pos[ 6]=pos[12]; pos[12]=c;
}



//convert string position to a position1 shape object.
void Position2::Initialise(Position1 p1){
	char prm[9], a,b;
	char* inipos=p1.Strng();
	prm[8]=0;

	ml=p1.GetMiddle();

	//Strip out corners
	for(a=0;a<8;a++) prm[a]=inipos[a*3+1];
	//convert to number
	cornperm=Perm2Num(prm,8);

	//Strip top layer edges
	if(inipos[0]==inipos[1]){	a=2; TopEdgeFirst=false; }
	else{						a=0; TopEdgeFirst=true;  }
	for(b=0; b<4; a+=3, b++) prm[b]=inipos[a];
	if(inipos[12]==inipos[13]){	a=14; BotEdgeFirst=false; }
	else{						a=12; BotEdgeFirst=true;  }
	for(   ; b<8; a+=3, b++) prm[b]=inipos[a];
	edgeperm=Perm2Num(prm,8);
}
