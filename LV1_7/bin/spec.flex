// import sekcija

// korisnicki kod

// koristi se za ukljucivanje dodatnih paketa

%%

// sekcija opcija i deklaracija

// opcije za podesavanje generisanog leksera
// jflex direktive 
// java kod koji se ukljucuje u razlicite delove leksera

%class MPLexer18329 // ime klase
%function next_token18329 // ime f-je koja vrsi izdvajanje jedn reci
%line // brojac linija u izvornom fajlu
%column // brojac karaktera u tekucoj liniji
%debug // generise main metodu koja poziva lek analizator sve dok ne dodje do kraja ulaznog fajla i u konzoli ispisuje sve izdvojene tokene kao i kod akcije koja je tada izvrsena
%type Yytoken //postavlja povratni tip f-je koja vrsi leksicku analizu

%eofval{ // deo koda se u dodaje u f-ju lek analize na mestu koje odg akciji kada se prepozna EOF. Ovaj deo koda vraca vrednost koja signalizira da je doslo do kraja koda koji se analizira
return new Yytoken( sym.EOF, null, yyline, yycolumn);
%eofval}

%{
//dodatni clanovi generisane klase
KWTable kwTable = new KWTable();
Yytoken getKW()
{
	return new Yytoken( kwTable.find( yytext() ),
	yytext(), yyline, yycolumn );
}
%}

//stanja
%xstate KOMENTAR

//makroi
slovo = [a-zA-Z]
cifra10 = [0-9]
cifra8 = [0-7]
cifra16 = [0-9a-fA-F]

%%

// pravila

\% { yybegin( KOMENTAR ); } //kazemo lek analizatoru da kada naidje na % da predje u pocetno stanje KOMENTAR i u tom stanju se prihvataju samo pravila koja prethodno pocinju nazivom stanja
<KOMENTAR>~\% { yybegin( YYINITIAL ); }

[\t\n\r ] { ; } //ako se prepozna tab, new line ili space onda se generise samo ;

\( { return new Yytoken( sym.LEFTPAR, yytext(), yyline, yycolumn ); }
\) { return new Yytoken( sym.RIGHTPAR, yytext(), yyline, yycolumn ); }
\{ { return new Yytoken( sym.LEFTBRACE, yytext(), yyline, yycolumn ); }
\} { return new Yytoken( sym.RIGHTBRACE, yytext(), yyline, yycolumn ); }

//operatori
= { return new Yytoken( sym.ASSIGN, yytext(), yyline, yycolumn ); }
&& { return new Yytoken( sym.AND, yytext(), yyline, yycolumn ); }
"||" { return new Yytoken( sym.OR, yytext(), yyline, yycolumn ); }
\< { return new Yytoken( sym.LESS, yytext(), yyline, yycolumn ); }
\<= { return new Yytoken( sym.LESS_EQUAL, yytext(), yyline, yycolumn ); }
== { return new Yytoken( sym.EQUAL, yytext(), yyline, yycolumn ); }
\!= { return new Yytoken( sym.NOT_EQUAL, yytext(), yyline, yycolumn ); }
> { return new Yytoken( sym.GREATER, yytext(), yyline, yycolumn ); }
>= { return new Yytoken( sym.GREATER_EQUAL, yytext(), yyline, yycolumn ); }

//separatori
; { return new Yytoken( sym.SEMICOLON, yytext(), yyline, yycolumn ); }
, { return new Yytoken( sym.COMMA, yytext(), yyline, yycolumn ); }

true { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }
false { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }


//kljucne reci
{slovo}+ { return getKW(); }

//identifikatori
({slovo}|_)({slovo}|{cifra10}|_)* { return new Yytoken(sym.ID, yytext(), yyline, yycolumn ); }

//konstante

	//int
("0#o"({cifra8}+)|"0#x"({cifra16}+)|("0#d")?({cifra10}+)) { return new Yytoken(sym.CONST, yytext(), yyline, yycolumn); }

	//char
'[^]' { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }

	//float
0\.{cifra10}+?(E[+-]?{cifra10}+)? { return new Yytoken( sym.CONST, yytext(), yyline, yycolumn ); }


//obrada gresaka
. { if (yytext() != null && yytext().length() > 0) System.out.println( "ERROR: " + yytext() ); }
