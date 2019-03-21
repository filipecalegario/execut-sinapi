package sinapi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class SINAPIEntry {

	private String classe;
	private String codigo;
	private String descricao;
	private String unidade;
	private String coeficiente;
	private EntryType type;
	private Map<String, SINAPIEntry> subItens;


	public SINAPIEntry(String classe, String codigo, String descricao, String unidade, String coeficiente, EntryType type) {
		this.classe = classe;
		this.codigo = codigo;
		this.descricao = descricao;
		this.unidade = unidade;
		this.coeficiente = coeficiente;
		this.type = type;
		this.subItens = new LinkedHashMap<String, SINAPIEntry>();
	}


	public SINAPIEntry() {
		// TODO Auto-generated constructor stub
	}


	public String getClasse() {
		return classe;
	}


	public void setClasse(String classe) {
		this.classe = classe;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getUnidade() {
		return unidade;
	}

//	public void explode(Map<String, SINAPIEntry> firstRoundMap) {
//		Vector<SINAPIEntry> itensToRemove = new Vector<>(); 
//		Vector<SINAPIEntry> itensToAdd = new Vector<>(); 
//		for (Map.Entry<String, SINAPIEntry> entrySubitem : this.getSubItens().entrySet()) {
//			SINAPIEntry subitem = entrySubitem.getValue();
//			if(subitem.getType().equals(EntryType.INDEFINIDO)){
//				for (Map.Entry<String, SINAPIEntry> entrySubsubitem : firstRoundMap.get(subitem.getCodigo()).getSubItens().entrySet()) {
//					SINAPIEntry subsubitem = entrySubsubitem.getValue();
//					itensToAdd.add(subsubitem);
//					System.out.println(subsubitem);
//					subsubitem.explode(firstRoundMap);
//				}
//				itensToRemove.add(subitem);
//			}
//		}
//		for (SINAPIEntry toDelete : itensToRemove) {
//			this.removeSubitem(toDelete);
//		}
//		for (SINAPIEntry toAdd : itensToAdd) {
//			this.addSubitem(toAdd);
//		}
//	}

//	public Map<String, SINAPIEntry> explode2(Map<String, SINAPIEntry> firstRoundMap) {
//		Vector<SINAPIEntry> itensToRemove = new Vector<>(); 
//		Vector<SINAPIEntry> itensToAdd = new Vector<>();
//		HashMap<String, SINAPIEntry> result = new HashMap<String, SINAPIEntry>();
//		
//		if(this.getType().equals(EntryType.INDEFINIDO)){
//			if(this.getSubItens().size() == 0) {
//				Set<Entry<String, SINAPIEntry>> entrySet = firstRoundMap.get(this.getCodigo()).getSubItens().entrySet();
//				for (Map.Entry<String, SINAPIEntry> entry : entrySet) {
//					SINAPIEntry subitem = entry.getValue();
//					this.addSubitem(subitem);
//				}
//			}
//		}
//		
//		
//		for (SINAPIEntry toDelete : itensToRemove) {
//			this.removeSubitem(toDelete);
//		}
//		for (SINAPIEntry toAdd : itensToAdd) {
//			this.addSubitem(toAdd);
//		}
//
//		return result;
//	}

//	public void addSubitensToIndefinido(Map<String, SINAPIEntry> firstRoundMap) {
//		if(this.getType().equals(EntryType.INDEFINIDO)){
//			if(this.getSubItens().size() == 0) {
//				Set<Entry<String, SINAPIEntry>> entrySet = firstRoundMap.get(this.getCodigo()).getSubItens().entrySet();
//				for (Map.Entry<String, SINAPIEntry> entry : entrySet) {
//					SINAPIEntry subitem = entry.getValue();
//					this.addSubitem(subitem);
//				}
//			}
//		}
//	}
	
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getCoeficiente() {
		return coeficiente;
	}

	public void setCoeficiente(String coeficiente) {
		this.coeficiente = coeficiente;
	}

	public void addSubitem(SINAPIEntry subItem){
		if(this.subItens != null){
			this.subItens.put(subItem.getCodigo(), subItem);
		}
	}

	public void removeSubitem(SINAPIEntry itemToRemove) {
		if(itemToRemove != null) {
			this.subItens.remove(itemToRemove.getCodigo());
		}
	}

	public Map<String, SINAPIEntry> getSubItens() {
		return subItens;
	}


	public EntryType getType() {
		return type;
	}

	public void setType(EntryType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type + " - " + classe + " " + codigo + " " + descricao + " " + unidade + " " + coeficiente ;
	}


	public void appendItens(Map<String, SINAPIEntry> subItensDoIndefinido) {
		//TODO Possível ponto de erro
		this.subItens.putAll(subItensDoIndefinido);
	}

}
