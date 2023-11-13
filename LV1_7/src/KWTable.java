
import java.util.Hashtable;
public class KWTable { // KeyWordTable - kako bi smo razlikovali kljucne reci i promenljive koje se sastoje samo od slova

	private Hashtable mTable;
	public KWTable()
	{
		/*// Inicijalizcaija hash tabele koja pamti kljucne reci
		mTable = new Hashtable();
		mTable.put("program", sym.PROGRAM);
		mTable.put("var", sym.VAR);
		mTable.put("integer", sym.INTEGER);
		mTable.put("char", sym.CHAR);
		mTable.put("begin", sym.BEGIN);
		mTable.put("end", sym.END);
		mTable.put("read", sym.READ);
		mTable.put("write", sym.WRITE);
		mTable.put("if", sym.IF);
		mTable.put("then", sym.THEN);
		mTable.put("else", sym.ELSE);*/
		
		//zadatak 7
		// Inicijalizcaija hash tabele koja pamti kljucne reci
		mTable = new Hashtable();
		mTable.put("main", sym.MAIN);
		mTable.put("loop", sym.LOOP);
		mTable.put("redo", sym.REDO);
		mTable.put("int", sym.INT);
		mTable.put("char", sym.CHAR);
		mTable.put("float", sym.FLOAT);
		mTable.put("bool", sym.BOOL);
	}
	
	/**
	 * Vraca ID kljucne reci 
	 */
	public int find(String keyword)
	{
		Object symbol = mTable.get(keyword);
		if (symbol != null)
			return ((Integer)symbol).intValue();
		
		// Ako rec nije pronadjena u tabeli kljucnih reci radi se o identifikatoru
		return sym.ID;
	}
	

}
