#ifndef __MOVELIST__
#define __MOVELIST__
#include <ostream>

using std::endl;

class MoveList {

	char list[100];		//actual list of moves
	int lngth;			//length of list;
	int lngthtwst;		//number of twists

public:
	MoveList(): lngth(0), lngthtwst(0) {}
	inline void push(char m){ 
		list[lngth++]=m; if(m==0) lngthtwst++;
	}
	inline char pull() {
		if(lngth){
			if(list[--lngth]==0) lngthtwst--;
			return list[lngth];
		}else{
			return 0;
		}
	}
	inline int readlist(char a){ return a<lngth? list[a] : 0; }
	inline int length() { return lngth; }
	inline int lengthtwist() { return lngthtwst; }
	inline void outputturn(std::ostream& os, int a){
		if(a<=6) os<<' '<<a;
		else os<<'-'<<(12-a);
	}
	inline void outputmove(std::ostream& os, int a, int b){
		os<<'(';
		outputturn(os,a);
		os<<',';
		outputturn(os,b);
		os<<')';
	}
	inline void print(std::ostream& os){
		int b,c;
		for(int a=0; a<lngth; a++){
			b=list[a];
			if(b==0) os<<'/';
			else if(b>0){
				if(a<lngth-1 && (c=list[a+1])<0){
					outputmove(os,b,-c); a++;
				}else outputmove(os,b,0);
			}else if(b<0) outputmove(os,0,-b);
				
		}
		/* os<<"    ["<<lngthtwst<<'|'<<lngth<<']'<<endl; */
	}
};

#endif
