package elasticsearch_file.sender.city;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;

import elasticsearch_file.GeneralElasticsearch;

public class City {

	private static BulkRequestBuilder bulkRequest;

	private static int total;

	private static String type = "city";

	public void write(String city, String nickname, String state, String npa, String nxx)
			throws ElasticsearchException, Exception {
		
		if(bulkRequest==null){
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("city").value(city);
		doc.field("city_state").value(city+"-"+state);
		doc.field("nickname").value(nickname);
		doc.field("state").value(state);
		doc.field("npa").value(npa);
		doc.field("nxx").value(nxx);
		doc.endObject();

		System.out.println(total+"--"+doc.string());
		
		bulkRequest.add(GeneralElasticsearch.getClient().prepareIndex("randomnumbers", type).setSource(doc));
		
		total++;
		
		if(total%10000==0){
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.out.println("Sent requests "+total);
		}
	}

	public void sendToElasticsearc(String city, String nickname, String state, String npa,
			String nxx) {

		try {
			this.write(city, nickname, state, npa, nxx);
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
