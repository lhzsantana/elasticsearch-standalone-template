package elasticsearch_file.sender.businessfax;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;

import elasticsearch_file.GeneralElasticsearch;

public class BusinessFax {

	private static BulkRequestBuilder bulkRequest;

	private static int total;

	public void write(String type, String phone, String code)
			throws ElasticsearchException, Exception {

		if (bulkRequest == null) {
			bulkRequest = GeneralElasticsearch.getClient().prepareBulk();
		}

		XContentBuilder doc = jsonBuilder().startObject();
		doc.field("phone").value(phone);
		doc.field("code").value(code);
		doc.field("complete").value(code + phone);
		doc.endObject();

		bulkRequest.add(GeneralElasticsearch.getClient().prepareIndex("randomnumbers", type)
				.setSource(doc));

		total++;

		if (total % 5000 == 0) {
			bulkRequest.execute().actionGet();
			bulkRequest = null;
			System.out.println("Sent requests " + total);
			;
		}
	}

	public void sendToElasticsearch(String type, String phone) {

		if (!phone.equals("") && phone.length() == 10) {

			// System.out.println(phone);

			String id = phone.substring(0, 3);
			phone = phone.substring(3, phone.length());

			System.out.println(type);
			System.out.println(id);
			System.out.println(phone);

			try {
				this.write(type, phone, id);
			} catch (ElasticsearchException e) {
				// TODO Auto-generated catch block e.printStackTrace();
			} catch (Exception e) { // TODO Auto-generated catch block
									// e.printStackTrace(); }
			}
		}
	}

}
