package elasticsearch_file.not_call;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class NotCall_Reader {

	public static void main(String[] args) throws Exception {
		NotCall_Reader reader = new NotCall_Reader();
		reader.read(new File("input.xml"));
	}

	public void read(File csvFile) throws Exception {

		if (csvFile == null)
			throw new Exception();

		NotCall es = new NotCall();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			String npa = "", phone = "", nxx = "", completo = "";
			
			int i = 0;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				if (line.contains("ac")) {
					if(line.length()>9){
						npa = line.toString().substring(9, 12);
					}else{
						continue;
					}
				}

				if (line.contains("ph")) {
					phone = line.toString().substring(9, 16);
					nxx=phone.substring(0, 3);
					completo=npa+phone;
	
					es.sendToElasticsearch(npa, nxx, completo);
					
					System.out.println(npa+nxx+completo);
				}

				i++;
				// if(i==2) break;
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
}