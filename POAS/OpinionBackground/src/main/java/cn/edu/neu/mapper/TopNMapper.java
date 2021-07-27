package cn.edu.neu.mapper;

import cn.edu.neu.po.TopN;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 32098
 */
@Repository
public interface TopNMapper {
    /**
     * 获取TopN
     *
     * @return TopN
     */
    @Select("SELECT * FROM topn ORDER BY modify_time DESC limit 1")
    TopN getTopN();

    /**
     * 设置TopN
     *
     * @param articleRank 文章排名的TopN
     * @param articleHotRank 文章热度的TopN
     * @param eventCommentRank 事件评论的TopN
     */
    @Select("INSERT INTO topn VALUES (#{articleRank}, #{articleHotRank}, #{eventCommentRank}, now())")
    void setTopN(int articleRank, int articleHotRank, int eventCommentRank);
}
