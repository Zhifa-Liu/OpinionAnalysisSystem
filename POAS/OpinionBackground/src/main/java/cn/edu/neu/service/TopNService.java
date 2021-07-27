package cn.edu.neu.service;

import cn.edu.neu.mapper.LogMapper;
import cn.edu.neu.mapper.TopNMapper;
import cn.edu.neu.po.TopN;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 32098
 */
@Service
public class TopNService {
    @Autowired
    private TopNMapper topnMapper;
    @Autowired
    private LogMapper logMapper;

    /**
     *
     * @return topN
     */
    TopN getTopN(){
        return topnMapper.getTopN();
    }

    public void setTopN(int articleRank, int articleHotRank, int eventCommentRank){
        topnMapper.setTopN(articleRank, articleHotRank, eventCommentRank);
        logMapper.setLog("事件文章、评论topN配置","设置文章排名=top"+articleRank+";文章热度排名=top"+articleHotRank+";文章评论排名=top"+eventCommentRank);
    }
}
