#ifndef __LAYERINCLUDED__
#define __LAYERINCLUDED__

#include <string.h>

class HalfLayer {
public:
	char Name[7];	// string with C/E's to show piece order
	char Turns[6];	// Turns which make layer twistable again (max of 5, ends in 0)
	char Corners, Edges, Pieces;	//number of pieces

public:
	//constructor
	HalfLayer(){};				//default empty constructor
	HalfLayer(const char*, const char*);
	void Set(const char*, const char*);

};


class Layer {
public:
	char Name[13];	// string with C/E's to show piece order
	unsigned char Turn;		// Least turn (1-6) which make layer twistable again.
	bool TurnParity;		// Set if this turn changes the parity
	unsigned char NextLeft, NextRight;	// Numbers of next layerhalves when turned

	//constructor
	Layer(){};
	Layer(const HalfLayer&, const HalfLayer&);
	void Set(const HalfLayer&, const HalfLayer&);
	inline bool Equals(const char* s){ return strcmp( Name, s)==0; }
};

#endif