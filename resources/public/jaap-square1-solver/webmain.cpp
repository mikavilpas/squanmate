// The cheerp/clientlib.h header contains declarations
// for all the browser APIs.
#include <cheerp/clientlib.h>
#include <cheerp/client.h>
#include "EngineTwst.h"
#include "main.h"

using namespace std;
using namespace client;

// The class can of course have any name
// The [[jsexport]] attribute tells Cheerp to make
// the class available to JavaScript code
class [[cheerp::jsexport]] Square1Solver
{


 private:
  // The class is allowed to have member variables
  // but they should all be trivially destructible
  int depth = 20;
  int mode = 0;
  ostream* output=NULL; // not used when reading from input position

 public:
  Square1Solver() {
  }

  // input should be like this:
  // char* input = "A2B3C1D45E6F7G8H"; // see readme for format explanation
  client::String* solve(const String* startingPosition)
  {
    Engine* eng = (Engine*) new EngineTwst();

    // client is a C++ namespace that contains all browser APIs
    client::console.log("Initializing Jaap Scherpuis's Square-1 solver, based on code available from https://www.jaapsch.net/puzzles/square1.htm");

    //initialise its pruning tables
    eng->Initialise();
    eng->InitPermTable();

    string input = ((std::string) *startingPosition);
    Position1 position;
    if (ReadPosition(input.c_str(), position))
    {
      return new client::String("something wrong with input, see js console for details");
    }

    eng->DoSearch(position,depth,mode,output);

    return new client::String(eng->solution.c_str());
  }
};

// webMain is the entry point for web applications written in Cheerp.
// An entry point, even if empty, is still required
void webMain()
{
}
