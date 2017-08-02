//Phase 1 position

inline unsigned max(unsigned a, unsigned b){ return a>b?a:b;}
inline unsigned min(unsigned a, unsigned b){ return a<b?a:b;}

// global variables: Pruning/transition tables
#include "layer.h"
extern HalfLayer hl[13];			//the 13 possible half layers
extern Layer layers[13][13];		//the 13^2 theoretically possible layers made of two halves
extern char ShapeTable[13][13][13][13][2];		//pruning table; gods algorithm for shape+parity
									//It holds length of position till even parity cube.
									//the high bit is set if a twist changes parity.
extern char PermTable[2][40320];		//pruning table; #twists to solve corner|edge permutation
extern unsigned PermTwistTable[40320];		//transition table for twists
extern unsigned PermTopTable[40320];		//transition table for top layer turns
extern unsigned PermBotTable[40320];		//transition table for bottom layer turns

//An object embodying a position for the phase 1 search.
class Position1{
	char pos[25];		//Actual piece positions, as a string
	char t1,t2,b1,b2;	//The half layer shapes of the topleft/right, bottomright/left
	char pm;			//The piece permutation. Is 0/1 if even/odd.
	char ml;			//The middle layer shape (0=irrelevant, +1=square, -1=kite)

public:
	void Initialise(char[]);	//convert from string of position. Is already legal.

	// Moves allowed
	char Top();					//Do top layer turn.
	char Bottom();				//Do bottom layer turn.
	void Twist();				//Do twist

	//Lookup in pruning table
	inline char Depth(){ return (ShapeTable[t1][t2][b1][b2][pm]&127)-1; }

	//give middle layer shape
	inline char GetMiddle(){ return ml; }
	//zap middle layer shape
	inline void IgnoreMiddle(){ ml=0; }

	//return string
	inline char* Strng(){return pos;}

private:
	void TurnLayer(char*,char);	//turn pieces
	void TwistLayers();			//twist pieces

};



class Position2{
	unsigned edgeperm;		//number encoding the edge permutation 0-40319
	unsigned cornperm;		//number encoding the corner permutation 0-40319
	bool TopEdgeFirst;		//true if top layer starts with edge left of seam
	bool BotEdgeFirst;		//true if bottom layer starts with edge right of seam
	char ml;				//shape of middle layer (+/-1, or 0 if ignored)
public:
	void Initialise(Position1);

	//give middle layer shape
	inline char GetMiddle(){ return ml; }

	//Lookup in pruning table
	inline char Depth(){
		char l1,l2;
		switch(ml){
		case 1:
			l1=PermTable[0][edgeperm];
			l2=PermTable[0][cornperm];
			break;
		case 0:
			l1=min(PermTable[0][edgeperm],PermTable[1][edgeperm]);
			l2=min(PermTable[0][cornperm],PermTable[1][cornperm]);
			break;
		default:
			l1=PermTable[1][edgeperm];
			l2=PermTable[1][cornperm];
			break;
		}
		return max(l1,l2)-1;
	}

	// Moves allowed
	//Do top layer turn.
	inline char Top(){
		TopEdgeFirst=!TopEdgeFirst;
		if(TopEdgeFirst)	{ edgeperm=PermTopTable[edgeperm]; return 1;}
		else				{ cornperm=PermTopTable[cornperm]; return 2;}
	}
	//Do bottom layer turn.
	inline char Bottom(){
		BotEdgeFirst=!BotEdgeFirst;
		if(BotEdgeFirst)	{ edgeperm=PermBotTable[edgeperm]; return 1;}
		else				{ cornperm=PermBotTable[cornperm]; return 2;}
	}
	//Do twist
	inline void Twist(){
		edgeperm=PermTwistTable[edgeperm];
		cornperm=PermTwistTable[cornperm];
		ml=-ml;
	}
	//Check if can twist while keeping square layers
	inline bool CanTwist(){
		return TopEdgeFirst==BotEdgeFirst;
	}
	
	//Check if solved
	inline bool IsSolved(){
		return (edgeperm==0 && cornperm==0 && TopEdgeFirst==false && BotEdgeFirst==true && ml>=0);
	}

};