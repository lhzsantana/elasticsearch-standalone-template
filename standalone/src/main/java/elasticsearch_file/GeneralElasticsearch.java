package elasticsearch_file;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;

import templates_elasticsearch.standalone.App;

public class GeneralElasticsearch {
	
	private static Client client;
	private static TransportClient transportClient;

	private static String server="198.23.188.82";
	private static String port="9300";
	private static String cluster = "imdb";
	
	public static void main(String [] args) throws ElasticsearchException, Exception{
		
		getClient().prepareSearch("randomnumbers")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchAllQuery())
		        .execute()
		        .actionGet();
	}

	public static synchronized Client getClient() throws Exception {

		if (client == null) {
			Settings settings = ImmutableSettings.settingsBuilder()
					.put("cluster.name", cluster).build();
			transportClient = new TransportClient(settings);
			client = transportClient
					.addTransportAddress(new InetSocketTransportAddress(server,
							Integer.parseInt(port)));
		}

		return client;
	}

	public static void wipeType(String index, String type) throws ElasticsearchException, Exception{
		/*	
		GeneralElasticsearch.getClient().prepareDeleteByQuery(index)
        .setQuery(termQuery("_type", type))
        .execute()
        .actionGet();
		
		App.updateMessage("Data deleted from "+type);
		*/
		
	}
}
