#include<bits/stdc++.h>
#define int long long
#define pii pair<int,int>
using namespace std;

map<string,set<string>> first;

struct id{
    vector<string> x;
    bool operator<(const id& other)const{
        return x<other.x;
    }
};

set<string> Begin,End,All,isEmpty;
vector<id> idRecord;
map<id,int> idNum;

void getFirst(string s){
    for(auto x:idRecord){
        vector<string> a=x.x;
        if(a[0]==s){
            for(int i=1;i<a.size();i++){
                if(End.count(a[i])){
                    first[s].insert(a[i]);
                    break;
                }else if(a[i]=="EPSILON"){
                    break;
                }else if(a[i]==a[0]&&!isEmpty.count(a[i])){
                    break;
                }else if(a[i]==a[0]){
                    continue;
                }else{
                    if(!first[a[i]].empty()){
                        first[s].insert(first[a[i]].begin(),first[a[i]].end());
                    }else{
                        getFirst(a[i]);
                        first[s].insert(first[a[i]].begin(),first[a[i]].end());
                    }
                    if(!isEmpty.count(a[i])){
                        break;
                    }
                }
            }
        }
    }
}

void init(){
    vector<string> re;
    re.push_back("CompUnit");
    re.push_back("Block");
    re.push_back(".");
    re.push_back("Block");
    re.push_back("{");
    re.push_back("BlockItemList");
    re.push_back("}");
    re.push_back(".");
    re.push_back("BlockItemList");
    re.push_back("BlockItemList");
    re.push_back("BlockItem");
    re.push_back(".");
    re.push_back("BlockItemList");
    re.push_back("EPSILON");
    re.push_back(".");
    re.push_back("BlockItem");
    re.push_back("VarDecl");
    re.push_back(".");
    re.push_back("BlockItem");
    re.push_back("Stmt");
    re.push_back(".");
    re.push_back("VarDecl");
    re.push_back("int");
    re.push_back("VarDeclList");
    re.push_back(";");
    re.push_back(".");
    re.push_back("VarDeclList");
    re.push_back("VarDeclList");
    re.push_back(",");
    re.push_back("VarDef");
    re.push_back(".");
    re.push_back("VarDeclList");
    re.push_back("VarDef");
    re.push_back(".");
    re.push_back("VarDef");
    re.push_back("Ident");
    re.push_back(".");
    re.push_back("VarDef");
    re.push_back("Ident");
    re.push_back("=");
    re.push_back("Exp");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("Ident");
    re.push_back("=");
    re.push_back("Exp");
    re.push_back(";");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("Exp");
    re.push_back(";");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back(";");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("Block");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("if");
    re.push_back("(");
    re.push_back("Cond");
    re.push_back(")");
    re.push_back("Block");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("if");
    re.push_back("(");
    re.push_back("Cond");
    re.push_back(")");
    re.push_back("Block");
    re.push_back("else");
    re.push_back("Block");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("while");
    re.push_back("(");
    re.push_back("Cond");
    re.push_back(")");
    re.push_back("Block");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("Ident");
    re.push_back("=");
    re.push_back("getint");
    re.push_back("(");
    re.push_back(")");
    re.push_back(";");
    re.push_back(".");
    re.push_back("Stmt");
    re.push_back("printf");
    re.push_back("(");
    re.push_back("FormatString");
    re.push_back("ExpList");
    re.push_back(")");
    re.push_back(";");
    re.push_back(".");
    re.push_back("ExpList");
    re.push_back("ExpList");
    re.push_back(",");
    re.push_back("Exp");
    re.push_back(".");
    re.push_back("ExpList");
    re.push_back("EPSILON");
    re.push_back(".");
    re.push_back("Exp");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("Cond");
    re.push_back("LOrExp");
    re.push_back(".");
    re.push_back("PrimaryExp");
    re.push_back("(");
    re.push_back("Exp");
    re.push_back(")");
    re.push_back(".");
    re.push_back("PrimaryExp");
    re.push_back("Ident");
    re.push_back(".");
    re.push_back("PrimaryExp");
    re.push_back("IntConst");
    re.push_back(".");
    re.push_back("UnaryExp");
    re.push_back("PrimaryExp");
    re.push_back(".");
    re.push_back("MulExp");
    re.push_back("UnaryExp");
    re.push_back(".");
    re.push_back("MulExp");
    re.push_back("MulExp");
    re.push_back("*");
    re.push_back("UnaryExp");
    re.push_back(".");
    re.push_back("MulExp");
    re.push_back("MulExp");
    re.push_back("/");
    re.push_back("UnaryExp");
    re.push_back(".");
    re.push_back("MulExp");
    re.push_back("MulExp");
    re.push_back("%");
    re.push_back("UnaryExp");
    re.push_back(".");
    re.push_back("AddExp");
    re.push_back("MulExp");
    re.push_back(".");
    re.push_back("AddExp");
    re.push_back("AddExp");
    re.push_back("+");
    re.push_back("MulExp");
    re.push_back(".");
    re.push_back("AddExp");
    re.push_back("AddExp");
    re.push_back("-");
    re.push_back("MulExp");
    re.push_back(".");
    re.push_back("RelExp");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("RelExp");
    re.push_back("RelExp");
    re.push_back("<");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("RelExp");
    re.push_back("RelExp");
    re.push_back(">");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("RelExp");
    re.push_back("RelExp");
    re.push_back("<=");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("RelExp");
    re.push_back("RelExp");
    re.push_back(">=");
    re.push_back("AddExp");
    re.push_back(".");
    re.push_back("EqExp");
    re.push_back("RelExp");
    re.push_back(".");
    re.push_back("EqExp");
    re.push_back("EqExp");
    re.push_back("==");
    re.push_back("RelExp");
    re.push_back(".");
    re.push_back("EqExp");
    re.push_back("EqExp");
    re.push_back("!=");
    re.push_back("RelExp");
    re.push_back(".");
    re.push_back("LAndExp");
    re.push_back("EqExp");
    re.push_back(".");
    re.push_back("LAndExp");
    re.push_back("LAndExp");
    re.push_back("&&");
    re.push_back("EqExp");
    re.push_back(".");
    re.push_back("LOrExp");
    re.push_back("LAndExp");
    re.push_back(".");
    re.push_back("LOrExp");
    re.push_back("LOrExp");
    re.push_back("||");
    re.push_back("LAndExp");
    re.push_back(".");
    string s;
    for(int i=0;i<re.size();i++){
        s=re[i]; i++;
        id z; z.x.push_back(s);
        Begin.insert(s);
        All.insert(s);
        while(re[i]!="."){
            s=re[i];
            z.x.push_back(s);
            End.insert(s);
            All.insert(s);
            i++;
        }
        idRecord.push_back(z);
        idNum[z]=idRecord.size()-1;
        if(z.x[1]=="EPSILON") isEmpty.insert(z.x[0]);
    }
    for(auto a:Begin){
        End.erase(a);
    }
    for(auto s:Begin){
        getFirst(s);
    }
}

struct status{
    set<id> x;
    bool operator<(const status& other)const{
        if(x.size()!=other.x.size()){
            return x.size()<other.x.size();
        }else{
            return x<other.x;
        }
    }
};
set<status> res;
map<status,int> statusNum;
map<pair<int,string>,int> LR1Sg;
map<pair<int,string>,int> LR1reduce;

void getStatus(status now){
    int flag;
    do{
        flag = now.x.size();
        set<id> tmp;
        for(auto ida:now.x){
            string s="";
            set<string> firstTmp;
            for(int i=2;i<ida.x.size()-1;i++){
                if(s!=""){
                    if(End.count(ida.x[i])){
                        firstTmp.insert(ida.x[i]);
                        break;
                    }
                    firstTmp.insert(first[ida.x[i]].begin(),first[ida.x[i]].end());
                    if(!isEmpty.count(ida.x[i])) break;
                    if(i==ida.x.size()-2) firstTmp.insert(ida.x.back());
                }
                if(ida.x[i-1]=="."){
                    s=ida.x[i];
                    if(!Begin.count(s)) break;
                }
            }
            if(s==""||!Begin.count(s)) continue;
            firstTmp.erase("EPSILON");
            if(firstTmp.empty()) firstTmp.insert(ida.x.back());
            for(auto idb:idRecord){
                if(idb.x[0]!=s) continue;
                id z=idb; z.x.insert(z.x.begin()+1,".");
                if(z.x.back()=="EPSILON") z.x.pop_back();
                for(auto a:firstTmp){
                    z.x.push_back(a);
                    tmp.insert(z);
                    z.x.pop_back();
                }
            }
        }
        now.x.insert(tmp.begin(),tmp.end());
    }while(flag<now.x.size());
    if(res.count(now)) return ;
    statusNum[now]=res.size();
    res.insert(now);
    for(auto ida:now.x){
        for(auto s:ida.x){
            cout<<s<<" ";
        }
        cout<<"\n";
    }
    cout<<"\n\n-----------------------------------------------------------\n";
    for(auto ida:now.x){
        id idb=ida;
        if(idb.x[idb.x.size()-2]=="."){
            string s=idb.x.back(); idb.x.pop_back(),idb.x.pop_back();
            if(idb.x.size()==1){
                idb.x.push_back("EPSILON");
            }
            LR1reduce[{res.size()-1,s}]=idNum[idb];
        }
    }
    for(auto s:All){
        status z;
        for(auto ida:now.x){
            id idb=ida;
            int flag=0;
            for(int i=2;i<idb.x.size()-1;i++){
                if(idb.x[i-1]=="."){
                    if(idb.x[i]==s){
                        swap(idb.x[i-1],idb.x[i]);
                        flag=1;
                    }
                    break;
                }
            }
            if(flag){
                z.x.insert(idb);
            }
        }
        if(!z.x.empty()){
            getStatus(z);
        }
    }
}
void getLR1Sg(status now){
    for(auto s:All){
        status z;
        for(auto ida:now.x){
            id idb=ida;
            int flag=0;
            for(int i=2;i<idb.x.size()-1;i++){
                if(idb.x[i-1]=="."){
                    if(idb.x[i]==s){
                        swap(idb.x[i-1],idb.x[i]);
                        flag=1;
                    }
                    break;
                }
            }
            if(flag){
                z.x.insert(idb);
            }
        }
        if(z.x.empty()) continue;
        int flag;
        do{
            flag = z.x.size();
            set<id> tmp;
            for(auto ida:z.x){
                string s="";
                set<string> firstTmp;
                for(int i=2;i<ida.x.size()-1;i++){
                    if(s!=""){
                        if(End.count(ida.x[i])){
                            firstTmp.insert(ida.x[i]);
                            break;
                        }
                        firstTmp.insert(first[ida.x[i]].begin(),first[ida.x[i]].end());
                        if(!isEmpty.count(ida.x[i])) break;
                        if(i==ida.x.size()-2) firstTmp.insert(ida.x.back());
                    }
                    if(ida.x[i-1]=="."){
                        s=ida.x[i];
                        if(!Begin.count(s)) break;
                    }
                }
                if(s==""||!Begin.count(s)) continue;
                firstTmp.erase("EPSILON");
                if(firstTmp.empty()) firstTmp.insert(ida.x.back());
                for(auto idb:idRecord){
                    if(idb.x[0]!=s) continue;
                    id z=idb; z.x.insert(z.x.begin()+1,".");
                    if(z.x.back()=="EPSILON") z.x.pop_back();
                    for(auto a:firstTmp){
                        z.x.push_back(a);
                        tmp.insert(z);
                        z.x.pop_back();
                    }
                }
            }
            z.x.insert(tmp.begin(),tmp.end());
        }while(flag<z.x.size());
        LR1Sg[{statusNum[now],s}]=statusNum[z];
    }
}
void startGetLR(){
    status z; id z1;
    z1.x.push_back("CompUnit");
    z1.x.push_back(".");
    z1.x.push_back("Block");
    z1.x.push_back("#");
    z.x.insert(z1);
    getStatus(z);
    for(auto a:res){
        getLR1Sg(a);
    }
}

//---------------------------------------------------------------------------------------------

map<string,string> mpType;
set<char> single;

void init1(){
    mpType["!"]="NOT";
    mpType["!="]="NEQ";
    mpType["%"]="MOD";
    mpType["&&"]="AND";
    mpType["("]="LPARENT";
    mpType[")"]="RPARENT";
    mpType["*"]="MULT";
    mpType["+"]="PLUS";
    mpType[","]="COMMA";
    mpType["-"]="MINU";
    mpType["/"]="DIV";
    mpType[";"]="SEMICN";
    mpType["<"]="LSS";
    mpType["<="]="LEQ";
    mpType["="]="ASSIGN";
    mpType["=="]="EQL";
    mpType[">"]="GRE";
    mpType[">="]="GEQ";
    mpType["["]="LBRACK";
    mpType["]"]="RBRACK";
    mpType["break"]="BREAKTK";
    mpType["const"]="CONSTTK";
    mpType["continue"]="CONTINUETK";
    mpType["else"]="ELSETK";
    mpType["getint"]="GETINTTK";
    mpType["if"]="IFTK";
    mpType["int"]="INTTK";
    mpType["main"]="MAINTK";
    mpType["printf"]="PRINTFTK";
    mpType["return"]="RETURNTK";
    mpType["void"]="VOIDTK";
    mpType["while"]="WHILETK";
    mpType["{"]="LBRACE";
    mpType["||"]="OR";
    mpType["}"]="RBRACE";
    single.insert('+');
    single.insert('-');
    single.insert('*');
    single.insert('%');
    single.insert('(');
    single.insert(')');
    single.insert('{');
    single.insert('}');
    single.insert('[');
    single.insert(']');
    single.insert(',');
    single.insert(';');
}
vector<string> getInstring(){
    string str;
    queue<char> q;
    init1();
    vector<string> inString;
    char c;
    while(!q.empty()||scanf("%c",&c)!=EOF){
        if(!q.empty()) c=q.front(),q.pop();
        if(c>='0'&&c<='9'||c>='A'&&c<='Z'||c>='a'&&c<='z'||c=='_') str+=c;
        else if(c==' '||c=='\n'||c=='\t'){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
        }else if(c=='"'){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
            str+=c;
            for(;scanf("%c",&c)!=EOF;){
                if(c=='\"'){
                    if(str.back()=='\\') str+=c;
                    else{
                        str+=c;
                        break;
                    }
                }else{
                    str+=c;
                }
            }
            inString.push_back(str);
            str.clear();
        }else if(c=='/'){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
            if(scanf("%c",&c)==EOF){
                inString.push_back("/");
            }
            else if(c=='/'){
                for(;scanf("%c",&c)!=EOF&&c!='\n';);
            }else if(c=='*'){
                for(;scanf("%c",&c)!=EOF;){
                    if(c=='*'){
                        if(scanf("%c",&c)!=EOF&&c=='/'){
                            break;
                        }
                    }
                }
            }else{
                inString.push_back("/");
                q.push(c);
            }
        }else if(c=='|'||c=='&'){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
            scanf("%c",&c);
            str+=c;str+=c;
            inString.push_back(str);
            str.clear();
        }else if(c=='>'||c=='<'||c=='!'||c=='='){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
            str+=c;
            if(scanf("%c",&c)==EOF) inString.push_back(str),str.clear();
            else if(c=='='){
                str+='=';
                inString.push_back(str);
                str.clear();
            }else{
                inString.push_back(str);
                str.clear();
                q.push(c);
            }
        }else if(single.count(c)){
            if(!str.empty()){
                inString.push_back(str);
                str.clear();
            }
            str+=c;
            if(!str.empty())inString.push_back(str);str.clear();
        }
    }
    inString.push_back("#");
    return inString;
}

//-------------------------------------------------------------------------------------------------

void syntaxAnalyze(vector<string>& inString){
    stack<int> stateStack;
    stack<string> outStack;
    stateStack.push(0);
    outStack.push("#");
    int pos=0;
    for(;pos<inString.size();){
        if(mpType.count(inString[pos])){
            int nowState=stateStack.top();
            if(LR1Sg.count({nowState,inString[pos]})){
                outStack.push(inString[pos]);
                stateStack.push(LR1Sg[{nowState,inString[pos]}]);
                cout<<mpType[inString[pos]]<<" "<<inString[pos]<<"\n";
                pos++;
            }else{
                id ida=idRecord[LR1reduce[{nowState,inString[pos]}]];
                cout<<"<"<<ida.x[0]<<">\n";
                if(ida.x==idRecord[0].x){
                    break;
                }
                if(ida.x[1]=="EPSILON") ida.x.pop_back();
                for(int sum=ida.x.size()-1;sum;sum--){
                    stateStack.pop();
                    outStack.pop();
                }
                outStack.push(ida.x[0]);
                stateStack.push(LR1Sg[{stateStack.top(),outStack.top()}]);
            }
        }else if(inString[pos][0]>='0'&&inString[pos][0]<='9'){
            int nowState=stateStack.top();
            if(LR1Sg.count({nowState,"IntConst"})){
                outStack.push(inString[pos]);
                stateStack.push(LR1Sg[{nowState,"IntConst"}]);
                cout<<"INTCON "<<inString[pos]<<"\n";
                pos++;
            }else{
                id ida=idRecord[LR1reduce[{nowState,"IntConst"}]];
                cout<<"<"<<ida.x[0]<<">\n";
                if(ida.x==idRecord[0].x){
                    break;
                }
                if(ida.x[1]=="EPSILON") ida.x.pop_back();
                for(int sum=ida.x.size()-1;sum;sum--){
                    stateStack.pop();
                    outStack.pop();
                }
                outStack.push(ida.x[0]);
                stateStack.push(LR1Sg[{stateStack.top(),outStack.top()}]);
            }
        }else if(inString[pos][0]=='"'){
            int nowState=stateStack.top();
            if(LR1Sg.count({nowState,"FormatString"})){
                outStack.push(inString[pos]);
                stateStack.push(LR1Sg[{nowState,"FormatString"}]);
                cout<<"STRCON "<<inString[pos]<<"\n";
                pos++;
            }else{
                id ida=idRecord[LR1reduce[{nowState,"FormatString"}]];
                cout<<"<"<<ida.x[0]<<">\n";
                if(ida.x==idRecord[0].x){
                    break;
                }
                if(ida.x[1]=="EPSILON") ida.x.pop_back();
                for(int sum=ida.x.size()-1;sum;sum--){
                    stateStack.pop();
                    outStack.pop();
                }
                outStack.push(ida.x[0]);
                stateStack.push(LR1Sg[{stateStack.top(),outStack.top()}]);
            }
        }else if(inString[pos]!="#"){
            int nowState=stateStack.top();
            if(LR1Sg.count({nowState,"Ident"})){
                outStack.push(inString[pos]);
                stateStack.push(LR1Sg[{nowState,"Ident"}]);
                cout<<"IDENFR "<<inString[pos]<<"\n";
                pos++;
            }else{
                id ida=idRecord[LR1reduce[{nowState,"Ident"}]];
                cout<<"<"<<ida.x[0]<<">\n";
                if(ida.x==idRecord[0].x){
                    break;
                }
                if(ida.x[1]=="EPSILON") ida.x.pop_back();
                for(int sum=ida.x.size()-1;sum;sum--){
                    stateStack.pop();
                    outStack.pop();
                }
                outStack.push(ida.x[0]);
                stateStack.push(LR1Sg[{stateStack.top(),outStack.top()}]);
            }
        }else{
            int nowState=stateStack.top();
            if(LR1Sg.count({nowState,"#"})){
                outStack.push(inString[pos]);
                stateStack.push(LR1Sg[{nowState,"#"}]);
                cout<<"IDENFR "<<inString[pos]<<"\n";
                pos++;
            }else{
                id ida=idRecord[LR1reduce[{nowState,"#"}]];
                cout<<"<"<<ida.x[0]<<">\n";
                if(ida.x==idRecord[0].x){
                    break;
                }
                if(ida.x[1]=="EPSILON") ida.x.pop_back();
                for(int sum=ida.x.size()-1;sum;sum--){
                    stateStack.pop();
                    outStack.pop();
                }
                outStack.push(ida.x[0]);
                stateStack.push(LR1Sg[{stateStack.top(),outStack.top()}]);
            }
        }
    }
}

signed main () {
    freopen("testfile.txt","r",stdin);
    freopen("output.txt","w",stdout);
    init();
    startGetLR();
//    vector<string> inString=getInstring();
//    syntaxAnalyze(inString);
}