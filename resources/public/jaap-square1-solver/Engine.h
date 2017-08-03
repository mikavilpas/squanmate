#ifndef __ENGINE__
#define __ENGINE__

#include "movelist.h"
#include "layer.h"
#include "position.h"
#include "perm.h"
#include <ostream>

using namespace std;

class Engine {

protected:
	Position1 InitPos;		//position that engine is to solve.
	int MaxDepth;			//Maximum depth; given at start, or length of last soln found
	int Mode;				//0 to find one shortest, 1 to find all shortests.
	ostream* OutStrm;		//Place to output results to.
	MoveList moves;			//List of moves done so far
	char Length1, Length2;	//Current max length of search
	long Nodes1, Nodes1Low, Nodes2;	//Number of nodes visited in latest search

public:
	inline void Initialise() { InitLayers(); InitShapeTable(); }
	int DoSearch(Position1&, int, int, ostream*);

	virtual void InitPermTable()=0;

  int solutionsFound = 0;
  string solution;

private:
	virtual void InitShapeTable()=0;
	virtual int StartPhase1()=0;
	void InitLayers();
protected:
	void FoundSol(int l, bool);
};

#endif
