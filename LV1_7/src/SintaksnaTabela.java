import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;

public class SintaksnaTabela {
	
	Stack<Integer> stek;
	boolean prepoznat,greska;
	MPLexer18329 _lexer;
	int[][] tabela;
	HashMap<Integer,int[]> rule;
	
	public SintaksnaTabela(MPLexer18329 lexer)
	{
		rule = new HashMap<Integer, int[]>();
		stek = new Stack<Integer>();
		tabela = new int[20][13];

		_lexer = lexer;
				
		stek.push(19);//# 20-ti red 
		stek.push(0);// prvi red je startni simbol
		prepoznat = false;
		greska = false;
		InicijalizujTabelu();
		inicijalizujPravila();
	}
	
	private void InicijalizujTabelu()
	{
		//-1 error
		//-2 pop
		//-3 accept
		//10 #
		//ostalo - smena
		for(int i = 0 ; i < 20;i++)
			for(int j = 0 ; j < 13;j++)
				tabela[i][j] = -1; //Error na sva mesta
		
		tabela[0][0] = 0; // (loop ( Expression ) { Statement redo ( Expression ) ; Statement }, 0)
		tabela[1][8] = 1; // (AndExpressin Expression', 1)
		tabela[1][9] = 1; // (AndExpressin Expression', 1)
		tabela[2][2] = 3; // (E,3)
		tabela[2][4] = 3; // (E,3)
		tabela[2][5] = 3; // (E,3)
		tabela[2][11] = 3; // (E,3)
		tabela[2][6] = 2; // (|| loop AndExpression Expression', 2)
		tabela[3][8] = 4; // (Term AndExpression', 4)
		tabela[3][9] = 4; // (Term AndExpression', 4)
		tabela[4][2] = 6; // (E,6)
		tabela[4][4] = 6; // (E,6)	
		tabela[4][5] = 6; // (E,6)	
		tabela[4][6] = 6; // (E,6)
		tabela[4][11] = 6; // (E,6)
		tabela[4][7] = 5; // (&& Term AndExpression', 5)
		tabela[5][8] = 7; // (ID,7)
		tabela[5][9] = 8; // (CONST,8)
		tabela[6][0] = 9; // (RedoLoop, 9)
		tabela[6][8] = 10; // (ID = Expression ;, 10) 
		tabela[11][11] = 6; // (E,6)
		tabela[11][5] = 6; // (E,6)
		
		//Pop
		for(int i = 0 ; i < 12 ; i++)
			tabela[7+i][i] = -2; // Pop  [7,0] - [18,11]
		
		//Accept
		tabela[19][12] = -3; // Accept [17,11]
	}
	
	private void inicijalizujPravila()
	{
		rule.put(0,new int[] {7,8,1,9,10,6,18,8,1,9,11,6,12}); 		// loop ( Expression ) { Statement redo ( Expression ) ; Statement }
		rule.put(1,new int[] {3,2});          	    // AndExpression Expression'
		rule.put(2,new int[] {13,3,2});                // || AndExpression Expression'
		rule.put(3,new int[] {});  			 	    // E
		rule.put(4,new int[] {5,4});			// Term AndExpression'
		rule.put(5,new int[] {14,5,4});			        // && Term AndExpression'
		rule.put(6,new int[] {});			 	// E
		rule.put(7,new int[] {15});				// ID
		rule.put(8,new int[] {16});	            // CONST
		rule.put(9,new int[] {0});	            // RedoLoop 
		rule.put(10,new int[] {15,17,1,11});	            // ID = Expression ;
				 	
	}
	
	private int VratiKolonu(int tokenId)
	{
		switch(tokenId)
		{
		case sym.LOOP:
			return 0;
		case sym.LEFTPAR:
			return 1;
		case sym.RIGHTPAR:
			return 2;
		case sym.LEFTBRACE:
			return 3;
		case sym.SEMICOLON:
			return 4;
		case sym.RIGHTBRACE:
			return 5;
		case sym.OR:
			return 6;
		case sym.AND:
			return 7;
		case sym.ID:
			return 8;
		case sym.CONST:
			return 9;
		case sym.ASSIGN:
			return 10;
		case sym.REDO:
			return 11;
		case sym.EOF:
			return 12;
		}
		return -1;
	}
	
	private void PushNizUStack(int[] arr)
	{
		// Obilazimo niz za pravila i pushujemo ih unazad
		// Onda sta je sledece za obradu, bice na vrhu steka
		for(int i = arr.length - 1; i >= 0 ; i--)
			stek.push(arr[i]);
	}
	
	public boolean Analiza()
	{
		Yytoken next;
		try {
			
			next = _lexer.next_token18329();
			System.out.print(next.toString());
			do
			{
				int x = stek.peek();
				int y = VratiKolonu(next.m_index);
				System.out.println();
				System.out.println();
				
				System.out.println("stek izgleda: " + stek);
				System.out.println("next index je " + next.m_index);
				System.out.println("x je " + x);
				System.out.println("y je " + y);
				int k = tabela[x][y];
				switch(k)
				{
				case -1:
					//Error
					greska = true;
					break;
				case -2:
					//Pop
					stek.pop();	
					next = _lexer.next_token18329();
					System.out.print(next.toString());
					break;
				case -3: 
					//Accept
					prepoznat = true;
					break;
				default:
					//Pronadjeno pravilo
					stek.pop();
					PushNizUStack(rule.get(k));
					break;
				}
				
				
			}while(!(prepoznat || greska));
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return prepoznat;
	}
	
	public static void main(String[] args)
	{
		Reader r;
		try {
			r = new FileReader("src/testinput2");
			SintaksnaTabela ST = new SintaksnaTabela(new MPLexer18329(r));
			
			boolean result = ST.Analiza();
			if(result)
				System.out.println("Uspesno prepoznato!");
			else
				System.out.println("Nije prepoznato!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
