#include "Engine.h"

class EngineTwst : public Engine {

public:
	void InitPermTable();

private:
	void InitShapeTable();
	int StartPhase1();
	int Phase1(Position1,char,char);
	int StartPhase2(Position1, char);
	int Phase2(Position2,char,char);
};
