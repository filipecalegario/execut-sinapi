package sinapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



public class SINAPIProcessing {

	private static final String FILE_NAME = "CATALOGO_COMPOSICOES_ANALITICAS_EXCEL_05_2018.xls";

	public static void main(String[] args) {

		Map<String, SINAPIEntry> firstRoundServicesEntries = new LinkedHashMap<>();

		try {
			FileWriter fileWriter = new FileWriter("output.txt");
			PrintWriter printWriter = new PrintWriter(fileWriter);

			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
			Workbook workbook = WorkbookFactory.create(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			for (int i = 0; i < 4; i++) {
				// Skip the header
				iterator.next();
			}

			SINAPIEntry sEntryServico = null;
			SINAPIEntry sEntrySubItem = null;
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				String classe = currentRow.getCell(0).getStringCellValue();
				String codigo = currentRow.getCell(1).getStringCellValue();
				String descricao = currentRow.getCell(2).getStringCellValue();
				String unidade = currentRow.getCell(3).getStringCellValue();
				String coeficiente = currentRow.getCell(4).getStringCellValue();

				boolean isTituloServico = (!classe.equals("INSUMO") && !classe.equals("COMPOSICAO"));

				if(isTituloServico){
					sEntryServico = new SINAPIEntry(classe, codigo, descricao, unidade, coeficiente, EntryType.SERVICO);
				} else {            
					if(descricao.contains("COM ENCARGOS COMPLEMENTARES")){
						sEntrySubItem = new SINAPIEntry(classe, codigo, descricao, unidade, coeficiente, EntryType.MAODEOBRA);
					} else if(unidade.contains("CHP") || unidade.contains("CHI")){
						sEntrySubItem = new SINAPIEntry(classe, codigo, descricao, unidade, coeficiente, EntryType.EQUIPAMENTO);
					} else if(classe.equals("INSUMO")){
						sEntrySubItem = new SINAPIEntry(classe, codigo, descricao, unidade, coeficiente, EntryType.MATERIAL);
					} else {
						sEntrySubItem = new SINAPIEntry(classe, codigo, descricao, unidade, coeficiente, EntryType.INDEFINIDO);
					}
					sEntryServico.addSubitem(sEntrySubItem);
				}
				//Para atualizar os EntryServicos completos com todos os subitens attached
				if(firstRoundServicesEntries.containsKey(sEntryServico.getCodigo())){
					firstRoundServicesEntries.remove(sEntryServico.getCodigo());
				}
				firstRoundServicesEntries.put(sEntryServico.getCodigo(), sEntryServico);
			}

			int count = 0;
			/*for (Map.Entry<String, SINAPIEntry> entryPrincipal : firstRoundServicesEntries.entrySet()) {
				SINAPIEntry principal = entryPrincipal.getValue();
				Vector<SINAPIEntry> itensToRemove = new Vector<>(); 
				Vector<SINAPIEntry> itensToAdd = new Vector<>(); 
				for (Map.Entry<String, SINAPIEntry> entrySubitem : principal.getSubItens().entrySet()) {
					SINAPIEntry subitem = entrySubitem.getValue();
					if(subitem.getType().equals(EntryType.INDEFINIDO)){
						//if(principal.getCodigo().equals("87620") && subitem.getCodigo().equals("87301")) {
							System.out.println(subitem);
							Float multiplicador = subitem.getCoeficiente();
							for (Map.Entry<String, SINAPIEntry> entrySubsubitem : firstRoundServicesEntries.get(subitem.getCodigo()).getSubItens().entrySet()) {
								SINAPIEntry subsubitem = entrySubsubitem.getValue();
								System.out.println("- " + subsubitem);
								subsubitem.setCoeficiente(subsubitem.getCoeficiente()*multiplicador);
								//principal.addSubitem(subsubitem);
								itensToAdd.add(subsubitem);
							}
							itensToRemove.add(subitem);
						//}
						//principal.removeSubitem(subitem);
						//System.out.println(uniqueEntries.containsKey(subitem.getCodigo()));
						//count++;
					}
					//printWriter.printf("%s,%s,%s,%s\n",principal.getCodigo(),subitem.getCodigo(),"1","Default");
				}
				for (SINAPIEntry toDelete : itensToRemove) {
					principal.removeSubitem(toDelete);
				}
				for (SINAPIEntry toAdd : itensToAdd) {
					principal.addSubitem(toAdd);
				}
				//printWriter.println(principal);
			}*/
			//System.out.println("Count: " + count);

			for (Map.Entry<String, SINAPIEntry> entryPrincipal : firstRoundServicesEntries.entrySet()) {
				SINAPIEntry principal = entryPrincipal.getValue();
				//if(principal.getCodigo().equals("93206")) {
					explode(principal, firstRoundServicesEntries);
				//}
			}
			
			for (Map.Entry<String, SINAPIEntry> entryPrincipal : firstRoundServicesEntries.entrySet()) {
				SINAPIEntry principal = entryPrincipal.getValue();
				printWriter.println(principal);
				for (Map.Entry<String, SINAPIEntry> entrySubitem : principal.getSubItens().entrySet()) {
					SINAPIEntry subitem = entrySubitem.getValue();
					printWriter.println(" - " + subitem);
				}
			}

			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void explode(SINAPIEntry currentItem, Map<String, SINAPIEntry> entryReferences){
		Map<String, SINAPIEntry> itensToAdd = new LinkedHashMap<>();
		Vector<SINAPIEntry> itensToRemove = new Vector<>();

		Map<String, SINAPIEntry> subItens = currentItem.getSubItens();
		Set<Entry<String, SINAPIEntry>> entrySet = subItens.entrySet();
		
		for (Map.Entry<String, SINAPIEntry> entry : entrySet) {
			SINAPIEntry subItem = entry.getValue();
			if(subItem.getType().equals(EntryType.INDEFINIDO)) {
				String codigoDoSubItem = subItem.getCodigo();
				Map<String, SINAPIEntry> subItensDoIndefinido = entryReferences.get(codigoDoSubItem).getSubItens();
				subItem.appendItens(subItensDoIndefinido);
				explode(subItem, entryReferences);
				itensToAdd.putAll(subItem.getSubItens());
				itensToRemove.add(subItem);
			}
		}
		
		currentItem.appendItens(itensToAdd);		
		
		for (SINAPIEntry toDelete : itensToRemove) {
			currentItem.removeSubitem(toDelete);
		}
		
	}
	
}

