#include "layer.h"

//constructor
HalfLayer::HalfLayer(const char* s1, const char* s2){ Set(s1,s2); }

void HalfLayer::Set(const char* s1, const char* s2){
	strcpy(Name,  s1);
	strcpy(Turns, s2);
	Corners=Edges=0;
	int i=0;
	while( Name[i] ){
		if(Name[i]=='E') Edges++; else Corners++;
		i++;
	}
	Corners>>=1;
	Pieces=Edges+Corners;
}


//constructor
Layer::Layer(const HalfLayer& h1, const HalfLayer& h2){ Set(h1,h2); }

void Layer::Set(const HalfLayer& h1, const HalfLayer& h2){
	strcpy(Name, h1.Name);
	strcat(Name, h2.Name);

	int t=(h1.Pieces+h2.Pieces)&1;
	//get least turn that makes layer twistable again
	int i,j;
	i=j=0; Turn=6;
	while( h1.Turns[i]!=0 && h2.Turns[j]!=0 ){
		if     ( h1.Turns[i] > h2.Turns[j] ) j++;
		else if( h1.Turns[i] < h2.Turns[j] ) i++;
		else { Turn=h1.Turns[i]-'0'; break; }
	}

	//calculate the permutation parity of this move;
	bool par = false;
	if(t==0){
		for(t=0;t<Turn;t++){
			if( Name[11-t]!='c' ) par=!par;
		}
	}
	TurnParity=par;
}