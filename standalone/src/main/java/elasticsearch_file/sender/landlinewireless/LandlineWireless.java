package elasticsearch_file.sender.landlinewireless;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;

import templates_elasticsearch.standalone.App;
import elasticsearch_file.GeneralElasticsearch;

public class LandlineWireless {

	private static BulkRequestBuilder bulkRequest;
	public static int total=0;

	public void write(String type, String phone, String npa, String nxx)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}
		
		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("phone").value(phone);
		doc.field("npa").value(npa);
		doc.field("nxx").value(nxx);
		doc.field("complete").value(npa+nxx+phone);
		doc.endObject();

		bulkRequest.add(GeneralElasticsearch.getClient().prepareIndex("randomnumbers", type)
				.setSource(doc));
		total++;
		if(total%15000==0){
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.out.println(total+" document indexed");
		}
		
	}

	public void sendToElasticsearc(String type, String phone, String npa,
			String nxx) {

		try {
			this.write(type, phone, npa, nxx);
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
