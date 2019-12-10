package ES;

import ES.utils.ESOperator;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.util.*;

import static ProgressBar.ProgressBar.printProgressBar;
import static ProgressBar.ProgressBar.endProgressBar;

public class UpdateOrReIndex {
    /**
     * PUT i_syl_per_500/_settings
     * { "index" : { "max_result_window" : 5000000}}
     */
    public static int[] TEST_EXAMPLE = {
            100,500,
            1000,1500,
            2000,2500,
            3000,3500,
            4000,4500,
            5000};

    public static String INDEX_100W = "index_import";
    public static String INDEX_500W = "i_syl_per_500";
    public static String INDEX_1000W = "i_np_hb";
    public static String TYPE_100W = "default";
    public static String TYPE_500W = "i_syl_per_500";
    public static String TYPE_1000W = "t_aj_np";
    public static String UPDATE_FIELD = "L_LINENUMBER";

    public static void main(String[] args) throws Exception{
        ESOperator operator = new ESOperator();
        LinkedList<Long> uTimes = new LinkedList<>();
        LinkedList<Long> iTimes = new LinkedList<>();

        // System.out.println(operator.simpleGet(INDEX_500W,TYPE_500W,"4133137es about the slyly s").getSourceAsString());
        // operator.simpleUpdate(INDEX_500W,TYPE_500W,"4133137es about the slyly s",ESOperator.map2ESJson(new HashMap<String, String>() {{
        //                 put(UPDATE_FIELD, "3");
        //             }}));
        // System.out.println(operator.simpleGet(INDEX_500W,TYPE_500W,"4133137es about the slyly s").getSourceAsString());

        for(int test: TEST_EXAMPLE) {
            /*
                获取待更新的 IDs
             */
            System.out.println("Searching IDs");
            LinkedList<String> ids = new LinkedList<>();
            SearchHits hits = operator.simpleSearch(INDEX_500W, TYPE_500W, test);
            for (SearchHit hit : hits) {
                ids.add(hit.getId());
            }
            hits = null;
            System.out.println("Searching Done");

            /*
                生成测试用Json
             */
            XContentBuilder uJson = null;
            try {
                uJson = ESOperator.map2ESJson(new HashMap<String, String>() {{
                    put(UPDATE_FIELD, "2");
                }});
            } catch (Exception e) {
                e.printStackTrace();
            }
            XContentBuilder iJson = null;
            try {
                iJson = ESOperator.map2ESJson(new HashMap<String, String>() {{
                    put(UPDATE_FIELD, "3");
                }});
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
                测试 UPdate时间
             */
            System.out.println("Testing Update with :"+test);
            long uStart = new Date().getTime();
            for (int i=0; i<ids.size(); i++) {
                operator.simpleUpdate(INDEX_500W, TYPE_500W, ids.get(i), uJson);
                printProgressBar(ids.size(),i+1);
            }
            endProgressBar();
            long uEnd = new Date().getTime();
            uTimes.add(uEnd - uStart);
            // for (String id : ids) {
            //     GetResponse response = operator.simpleGet(INDEX_500W, TYPE_500W, id);
            //     if (!"2".equals(response.getField(UPDATE_FIELD))){
            //         System.out.println("Wrong! With : "+response.getField(UPDATE_FIELD));
            //     }
            // }

            /*
                测试 Index时间
             */
            System.out.println("Testing Index with :"+test);
            long iStart = new Date().getTime();
            for (int i=0; i<ids.size(); i++) {
                operator.simpleIndex(INDEX_500W, TYPE_500W, ids.get(i), iJson);
                printProgressBar(ids.size(),i+1);
            }
            endProgressBar();
            long iEnd = new Date().getTime();
            iTimes.add(iEnd - iStart);
            // for (String id : ids) {
            //     GetResponse response = operator.simpleGet(INDEX_500W, TYPE_500W, id);
            //     if (!"3".equals(response.getField(UPDATE_FIELD))){
            //         System.out.println("Wrong! "+response.getId()+" With : "+response.getField(UPDATE_FIELD));
            //     }
            // }
        }
        System.out.println("<<<=========================UPDATE==========================>>>");
        uTimes.forEach((item)-> System.out.println(item));
        System.out.println("<<<=========================INDEX ==========================>>>");
        iTimes.forEach((item)-> System.out.println(item));
        // GetResponse response = operator.simpleGet("i_syl_per_500","i_syl_per_500","5086292ar foxes sleep ");
        // System.out.println(response.getSourceAsString());
    }

}
