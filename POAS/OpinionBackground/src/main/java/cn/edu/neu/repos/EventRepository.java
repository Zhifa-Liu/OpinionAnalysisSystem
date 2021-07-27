package cn.edu.neu.repos;

import cn.edu.neu.po.EventElasticSearch;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author 32098
 */
public interface EventRepository extends ElasticsearchRepository<EventElasticSearch, String> {
//    /**
//     * 根据前端输入返回事件
//     * @param input 前端的输入
//     * @return Events
//     */
//    @Query()
//    List<EventElasticSearch> searchByInput(String input);
}
