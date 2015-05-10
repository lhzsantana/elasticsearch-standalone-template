package elasticsearch_file.sender.businessfax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.elasticsearch.ElasticsearchException;

import elasticsearch_file.GeneralElasticsearch;

public class BusinessFax_Reader {

	public BusinessFax_Reader() throws ElasticsearchException, Exception{

	}
	
	
	public void read(String directory) throws ElasticsearchException, Exception {

		BusinessFax es = new BusinessFax();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		if(directory==null) directory="business";

		File folder = new File(directory);

		try {
			for (final File fileEntry : folder.listFiles()) {
				if (!fileEntry.isDirectory()) {
					
					int i = 0;
					br = new BufferedReader(new FileReader(fileEntry));
										
					while ((line = br.readLine()) != null) {
						
						if(i==0){

							GeneralElasticsearch.wipeType("randomnumbers","fax");
							GeneralElasticsearch.wipeType("randomnumbers","business");		
							
						}
						

						//System.out.println(fileEntry.getName());
						//System.out.println(line);
						// System.out.println(i);

						// use comma as separator
						String[] splitedLine = line.split(cvsSplitBy);
						// System.out.println(splitedLine.length);

						if (splitedLine.length > 16) {

							String fax = removeString(isNumber(splitedLine[16]));
							String phone = removeString(isNumber(splitedLine[15]));

							// System.out.println("Phone: " + phone);
							// System.out.println("Fax: " + fax );

							es.sendToElasticsearch("fax", fax);
							es.sendToElasticsearch("business", phone);
						}

						i++;
						//System.out.println(i);
						// if(i==2) break;
					}
				}
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

	public static String isNumber(String original) {
		try {
			double d = Double.parseDouble(original);
		} catch (NumberFormatException nfe) {
			return "";
		}
		return original;
	}
}
