void help();
int ReadSwitch(const char*, int&, int&);
int ReadPosition(const char*, Position1&);
int ReadMoves(const char*, Position1&, int&, int&);
int ReadFileName(const char*, ostream**);
void ParseError(const char*, const char*, char);
