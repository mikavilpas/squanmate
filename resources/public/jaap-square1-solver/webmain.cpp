// The cheerp/clientlib.h header contains declarations
// for all the browser APIs.
#include <cheerp/clientlib.h>

#include "EngineTwst.h"
#include "main.h"

// webMain is the entry point for web applications written in Cheerp.
void webMain()
{
  int a = 3;
  Engine* eng = (Engine*) new EngineTwst();

  // client is a C++ namespace that contains all browser APIs
  client::console.log("Initializing Jaap Scherpuis's Square-1 solver, https://www.jaapsch.net/puzzles/square1.htm");
  client::console.log(a);


	//initialise its pruning tables
	client::console.log("Initializing...");
	eng->Initialise();
	eng->InitPermTable();


	ostream* output=NULL; // not used when reading from input position
  char* input = "A2B3C1D45E6F7G8H"; // example 2 in readme
  Position1 position;
  ReadPosition(input, position);
  position.IgnoreMiddle();

  int depth = 20;
  int mode = -2;
  eng->DoSearch(position,depth,mode,output);
}
