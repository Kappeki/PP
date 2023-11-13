class Yytoken {
	public int m_index; // znacenje izdvojene reci (celobrojni identifikator), uzima neku od vrednosti iz sym klase
	public String m_text; //izdvojena rec
	public int m_line; // linija koda iz koje je rec izdvojena
	public int m_charBegin; //pozicija u liniji od koje je rec izdvojena
	//public Object value - za sada nam ovaj atribut nije bitan, tek kod sem analize. Ovaj atribut je vrednost kolona iz tabele sa prezentacije

	Yytoken(int index, String text, int line, int charBegin) {
		m_index = index;
		m_text = text;
		m_line = line;
		m_charBegin = charBegin;
	}

	public String toString() { //kako bi smo lakse pratili izlaz
		return "Text : " + m_text 
				+ "\nindex : " + m_index 
				+ "\nline : " + m_line 
				+ "\ncBeg. : " + m_charBegin;
	}
}