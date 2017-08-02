#include "perm.h"

/***************************************************
*             Conversion functions                 *
***************************************************/

void Num2Perm(char* perm, const char chr, unsigned num, const char n){
	/* convert perm number in range [0,8!> to permutation of 8 chars
		in permray, using characters chr to chr+7   */
	unsigned c;
	char a,b;
	char* w=new char[n];
	for(a=0;a<n;a++)w[a]=a+chr;
	for(a=0;a<n;a++){
		c=num/(n-a);
		b=num-c*(n-a);
		num=c;
		perm[a]=w[b];
		while(++b <n) w[b-1]=w[b];
	}
	delete w;
}

unsigned Perm2Num(const char* perm, const char n){
	/* convert permutation of n chars to number [0,n!> */
	unsigned p;
	char a,b,c;

	p=0;
	for(a=n;a--;){
		c=0;
		for(b=a+1;b<n;b++){
			if(perm[b]<perm[a]) c++;
		}
		p=p*(n-a)+c;
	}

	return(p);
}
