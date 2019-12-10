package ES.utils;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESOperator {

    private static final Logger logger = LoggerFactory.getLogger(ESOperator.class);
    private static final String CLUSTER_NAME = "esCluster";
    private static final boolean SSL_ENABLED = false;
    private static final String PING_TIMEOUT = "120s";
    private static final String THROTTLE_TYPE = "none";
    // private static final String
    private static final String HOST = "172.16.11.57";
    private static final int PORT = 9300;

    private TransportClient client;

    public ESOperator() {
        Settings settings = Settings.builder()
                .put("cluster.name", CLUSTER_NAME)
                .put("xpack.security.transport.ssl.enabled", SSL_ENABLED)
                .put("client.transport.ping_timeout", PING_TIMEOUT)
                .put("indices.store.throttle.type", THROTTLE_TYPE)
                .build();
        try {
            this.client = new PreBuiltXPackTransportClient(settings)
                    .addTransportAddress(
                            new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT)
                    );
        } catch (UnknownHostException e) {
            this.client = null;
            e.printStackTrace();
        }
        List<DiscoveryNode> nodes = this.client.connectedNodes();
        for (DiscoveryNode node : nodes) {
            logger.info("Match node : "+node.getHostAddress());
        }
    }

    public Client getClient(){
        return this.client;
    }

    public void close(){
        if(null != this.client){
            this.client.close();
        }
    }

    public IndexResponse simpleIndex(String index,String type,String id,XContentBuilder json){
        IndexResponse response = this.client
                .prepareIndex(index, type, id)
                .setSource(json)
                .get();
        return response;
    }

    public GetResponse simpleGet(String index,String type,String id){
        GetResponse response = this.client
                .prepareGet(index, type, id)
                .get();
        return response;
    }

    public UpdateResponse simpleUpdate(String index,String type,String id,XContentBuilder json){
        UpdateResponse response = this.client
                .prepareUpdate(index, type, id)
                .setDoc(json)
                .get();
        return response;
    }

    public SearchHits simpleSearch(String index,String type,int size){
        SearchResponse response = this.client.prepareSearch(index)
                .setTypes(type)
                .setSize(size)
                .get();
        return response.getHits();
    }

    public static XContentBuilder map2ESJson(Map<String,String> map) throws IOException {
        XContentBuilder builder = jsonBuilder().startObject();
        for(Map.Entry<String,String> entry: map.entrySet()){
            builder.field(entry.getKey(),entry.getValue());
        }
        builder.endObject();
        return builder;
    }
}
