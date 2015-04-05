package elasticsearch_file.sender.landlinewireless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.elasticsearch.ElasticsearchException;

import elasticsearch_file.GeneralElasticsearch;

public class LandlineWireless_Reader {

	public LandlineWireless_Reader() throws ElasticsearchException, Exception{

		LandlineWireless.total=0;	
		
	}

	public void read(File csvFile) throws Exception {

		if(csvFile==null) throw new Exception();

		LandlineWireless es = new LandlineWireless();
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			int i = -1;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				i++;
				
				//System.out.println("Line "+i);

				//if(i<300000){
				//	continue;
				//}
				
				if(i==0){
					GeneralElasticsearch.wipeType("randomnumbers","landline");		
					GeneralElasticsearch.wipeType("randomnumbers","wireless");	
				}
				
				 //System.out.println(line);
				//System.out.println(i);

				// use comma as separator
				//String[] splitedLine = line.split(cvsSplitBy);
				String[] splitedLine = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				//System.out.println(splitedLine.length);

				
				if (splitedLine.length >= 14) {

					String npa = splitedLine[0].replace("\"", "");
					String nxx = splitedLine[1].replace("\"", "");
					String phone = splitedLine[5].replace("\"", "");
					String state = splitedLine[3].replace("\"", "");
					String city = isNotNumber(splitedLine[6].replace("\"", ""));
					String type = splitedLine[9].replace("\"", "");

					if(!phone.trim().equals("")){
						phone=splitedLine[14].replace("\"", "");
					}
					
					
					if(type.equals("ILEC")||
							type.equals("ICO")||
							type.equals("RBOC")||
							type.equals("CLEC")||
							type.equals("LRSL")||
							type.equals("IXC")||
							type.equals("LRSL")||
							type.equals("PCS ")||
							type.equals("PAGING")||
							type.equals("CAP")||
							(type.equals("WIRE")||
							type.equals("WRSL"))){

							if(!type.trim().equals("") && !phone.trim().equals("") && isNumber(phone)){
								es.sendToElasticsearc(type, phone, npa, nxx);
								System.out.println(type);
								
							}else{
								//System.out.println("Line "+i+": " + type + "|" + phone  + "|" + npa + "|" +  nxx + "|" +  state + "|" +  city);
								
							}						
					}
					
				}
				
				//if(i==2) break;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");

	}

	public static String removeString(String original) {

		return original.replace("-", "");
	}

	public static boolean isNumber(String original) {
		try {
			double d = Double.parseDouble(original);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static String isNotNumber(String original) {
		try {
			double d = Double.parseDouble(original);
		} catch (NumberFormatException nfe) {
			return "";
		}
		return original;
	}
}
