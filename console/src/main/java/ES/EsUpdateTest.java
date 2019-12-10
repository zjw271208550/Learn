package ES;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 测试ES更新API性能
 * 基于现在的2节点，每个节点24G内存、16核CPU的ES集群，使用两个index分别进行结构化数据和文书数据的更新测试，结果如下：
 * 索引更新频率为1S：
 * ①结构化数据使用ES更新API进行数据更新
 * 单并发时更新速率：88条/秒
 * 在10并发时达到最高速率：625条/秒
 * ②文书数据使用ES更新API进行数据更新
 * 单并发时更新速率：7条/秒
 * 在10并发时达到最高速率：33条/秒
 * ③文书数据使用ES更新API进行更新结构化数据
 * 单并发时更新速率：11条/秒
 * 在10并发时达到最高速率：42条/秒
 *
 * 索引更新频率为-1：
 * ①结构化数据使用ES更新API进行数据更新
 * 单并发时更新速率：175条/秒
 * 在10并发时达到最高速率：1250条/秒
 * ②文书数据使用ES更新API进行数据更新文书数据
 * 单并发时更新速率：13条/秒
 * 在10并发时达到最高速率：59条/秒
 * ③文书数据使用ES更新API进行更新结构化数据
 * 单并发时更新速率：19条/秒
 * 在10并发时达到最高速率：71条/秒
 *
 * 索引更新频率为2S：
 * 更新速率与1S时一致
 * @author: songyulin
 * @create: 2019-11-13
 **/
public class EsUpdateTest {

    static TransportClient client = null;
    static {
        Settings settings = Settings.builder()
                .put("cluster.name", "esCluster")
                .put("xpack.security.transport.ssl.enabled", false)
                .put("client.transport.ping_timeout", "120s")
                .put("indices.store.throttle.type", "none")
                .build();
        try {
            client =new PreBuiltXPackTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.11.57"),9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    /**
     * 结构化数据使用ES更新API进行数据更新，单字段更新的内容大小小于20字节，单并发下每条数据更新平均用时11.4ms；
     * 通过修改更新字段数量和数据总量进行测试得到结论：更新用时与更新字段的数量和数据总量无关；
     * 通过修改并发数进行测试得到结论：并发数量增高会影响数据更新性能，并发数与更新速率的函数约为：T(单条数据更新时间)=LOG0.8N(并发数)+11。
     * 单线程更新文书等大字段数据速率约为145ms每条，并发数提升到10更新速率上升到最大，处理速率为31ms每条，每秒可处理33条更新数据。
     * 修改索引的更新频率会提高ES更新API处理速率，当把更新频率由原本的1s修改为-1后，更新频率提高到原本2倍。
     * @throws IOException
     */

    public void updateEsByUrb() throws IOException {
        //查询需要更新的1W数据信息
        String jsonStr = "{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  },\n" +
                "  \"size\": 10000\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, new ArrayList<SearchPlugin>());
        try ( XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser( new NamedXContentRegistry(searchModule.getNamedXContents()), jsonStr))
        {
            searchSourceBuilder.parseXContent(new QueryParseContext(parser));
        }
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);
        SearchResponse searchResponse = searchRequestBuilder.setSource(searchSourceBuilder).setIndices("search_dev").setTypes("aj")
                .execute().actionGet();
        //命中的1W条数据信息
        SearchHits searchHits = searchResponse.getHits();
        //需更新的字段信息Map，每次更新需要保证数据和原数据不一致
        HashMap<String,Object> data=new HashMap<>();
//        data.put("ajjafs","wegas");
        data.put("ajlb","爱我的213aw");
        //更新方法构造
        UpdateRequestBuilder urb= client.prepareUpdate();
        urb.setIndex("search_dev").setType("aj");
        //更新总时间记录
        long time = 0;
        int i = 0;
        for(SearchHit searchHit : searchHits){
            //data.put("QW.oValue",  ((HashMap)(searchHit.getSource().get("QW"))).get("oValue")+"awdawdasc");
            i++;
            //更新前时间
            long start = System.currentTimeMillis();
            //实现ID数据的更新
            urb.setId(searchHit.getId());
            urb.setDoc(data);
            urb.execute().actionGet();
            //更新后时间
            long end = System.currentTimeMillis();
            //记录更新一条数据所需要的时间
            time += (end-start);
        }
        System.out.println("更新"+i+"条数据,总共用时："+time+"ms");
    }


    public static void main(String[] args) {
        corruntUpdateEs();
        // changeIndexProperties("search_dev","1s","1");
        //changeIndexProperties("search_dev","-1","0");
        //changeIndexProperties("rcp","1s","1");
        //changeIndexProperties("rcp","-1","0");
    }


    /**
     *并发下进行ES更新API测试
     */
    public static void corruntUpdateEs(){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        try {
            for (int i = 1; i < 11; i++) {
                final int finalI = i;
                fixedThreadPool.submit(new Runnable() {
                    int num = 0;
                    @Override
                    public void run() {
                        try {
                            updateEsByUrb(finalI,num);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateEsByUrb(int i,int num) throws IOException {
        //查询需要更新的1W数据信息
        String jsonStr = "{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {\n" +
                "    }\n" +
                "  },\n" +
                "  \"size\": 100,\n" +
                "  \"from\": "+i+"00\n" +
                "}";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, new ArrayList<SearchPlugin>());
        try ( XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser( new NamedXContentRegistry(searchModule.getNamedXContents()), jsonStr))
        {
            searchSourceBuilder.parseXContent(new QueryParseContext(parser));
        }
        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);
        SearchResponse searchResponse = searchRequestBuilder.setSource(searchSourceBuilder).setIndices("rcp").setTypes("ws")
                .execute().actionGet();
        //命中的1W条数据信息
        SearchHits searchHits = searchResponse.getHits();
        //需更新的字段信息Map，每次更新需要保证数据和原数据不一致
        HashMap<String,Object> data=new HashMap<>();
//        data.put("ajjafs","wegas");
        data.put("ajlb","爱我的213aw");
        //更新方法构造
        UpdateRequestBuilder urb= client.prepareUpdate();
        urb.setIndex("rcp").setType("ws");
        //更新总时间记录
        long time = 0;
        for(SearchHit searchHit : searchHits){
            //data.put("QW.oValue",  ((HashMap)(searchHit.getSource().get("QW"))).get("oValue")+"搭嘎喝完酒12376a");
            num++;
            //更新前时间
            long start = System.currentTimeMillis();
            //实现ID数据的更新
            urb.setId(searchHit.getId());
            urb.setDoc(data);
            urb.execute().actionGet();
            //更新后时间
            long end = System.currentTimeMillis();
            //记录更新一条数据所需要的时间
            time += (end-start);
        }
        System.out.println("线程"+i+"更新"+num+"条数据,总共用时："+time+"ms");
    }


    /**
     *  通过更新索引的刷新频率可得刷新频率对更新API更新速率影响结果
     * @param index 索引名称
     * @param refresh_interval 刷新频率
     * @param number_of_replicas
     */
    private static void changeIndexProperties(String index,String refresh_interval, String number_of_replicas) {
        client.admin()
                .indices()
                .prepareUpdateSettings(index)
                .setSettings(
                        //version 5
                        Settings.builder().put("index.refresh_interval", refresh_interval)
                                .put("index.number_of_replicas", number_of_replicas)).get();
    }



//    //单线程进行ES数据插入覆盖
//    //更新和插入覆盖的方式不一样，所以使用单纯更新的方式
//    public static void updateEs() throws IOException {
//        //数据抽取 1W条
//        BulkRequest bulkRequest = new BulkRequest();
//        String jsonStr = "{\n" +
//                "  \"query\": {\n" +
//                "    \"match_all\": {}\n" +
//                "  },\n" +
//                "  \"size\": 10000\n" +
//                "}";
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, new ArrayList<SearchPlugin>());
//        try ( XContentParser parser = XContentFactory.xContent(XContentType.JSON)
//                .createParser( new NamedXContentRegistry(searchModule.getNamedXContents()), jsonStr))
//        {
//            searchSourceBuilder.parseXContent(new QueryParseContext(parser));
//        }
//        SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);
//        SearchResponse searchResponse = searchRequestBuilder.setSource(searchSourceBuilder).setIndices("search_dev").setTypes("aj")
//                .execute().actionGet();
//        SearchHits searchHits = searchResponse.getHits();
//        int i = 1;
//        System.out.println("数据开始采集");
//        for(SearchHit searchHit : searchHits){
//            i++;
//            IndexRequest request = new IndexRequest("search_dev","aj")
//                    .id(searchHit.getId()).source(searchHit.getSource(), XContentType.JSON);
//            bulkRequest.add(request);
//            if(i == 100000){
//                break;
//            }
//        }
//        //数据更新
//        long start = System.currentTimeMillis();
//        System.out.println("数据更新开始"+start);
//        BulkResponse bulkResponse = new RestHighLevelClient(restClient).bulk(bulkRequest);
//        long end = System.currentTimeMillis();
//        System.out.println("数据更新结束"+end);
//        System.out.println("共用时"+(end-start));
//        if (bulkResponse.hasFailures() ) {
//            System.out.println(bulkResponse.buildFailureMessage());
//        }
//    }


}
