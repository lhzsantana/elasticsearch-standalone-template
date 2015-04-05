package elasticsearch_file.not_call;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;

import elasticsearch_file.GeneralElasticsearch;

public class NotCall {

	private static BulkRequestBuilder bulkRequest;

	private static int total;

	private static String type = "notcall";

	public void write(String npa, String nxx, String completo)
			throws ElasticsearchException, Exception {
		
		if(bulkRequest==null){
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("npa").value(npa);
		doc.field("nxx").value(nxx);
		doc.field("completo").value(completo);
		doc.endObject();

		System.out.println(total+"--"+doc.string());
		
		bulkRequest.add(GeneralElasticsearch.getClient().prepareIndex("randomnumbers", type).setSource(doc));
		
		total++;
		
		if(total%10000==0){
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.gc();
			System.out.println("Sent requests "+total);
		}
	}

	public void sendToElasticsearch(String npa, String nxx, String completo) {

		try {
			this.write(npa, nxx, completo);
		} catch (ElasticsearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
