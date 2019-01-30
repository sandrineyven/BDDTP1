import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {	

	public Crawler(){
		
		Document codeSource = null;
		try {
			codeSource = Jsoup.connect("http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=1337").get();
		} catch (IOException e1) {
			System.out.println("La connection a echouee");
			e1.printStackTrace();
		}
		
		//
		Spell spell = new Spell();
		
		//name
		Elements divSpell = codeSource.select("div.SpellDiv");
		Elements spell1 = divSpell.select("div.heading");
		spell.setName( spell1.select("p").first().text());
		
		//level
		Elements level1 = divSpell.select("p.SPDet").first().getAllElements();
		
		//System.out.println(name.text());
		String[] liste = level1.text().split(" ");
		
		for(int i =0; i< liste.length; i++){
			//System.out.println(liste[i]);
			if(liste[i].equals("sorcerer/wizard")){
				spell.setLevel(liste[i+1].replaceAll(",", ""));
			}
		}
		
		if(spell.getLevel() == null){
			for(int i =0; i< liste.length; i++){
				if(liste[i].equals("Level")){
					spell.setLevel(liste[i+2].replaceAll(",", ""));
					break;
				}
			}
		}
		
		//TODO: Récuperer le level, les composants et spell resitance (boolean)
		//EN faire une boucle sur toute les pages de 1 à 1600
		
		System.out.println(divSpell);
		
		System.out.println(spell.getName() + " " + spell.getLevel());
	}
}
