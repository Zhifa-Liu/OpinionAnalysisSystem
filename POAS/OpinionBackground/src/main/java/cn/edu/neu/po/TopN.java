package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopN {
    private int articleRank;
    private int articleHotRank;
    private int eventCommentRank;
    private String modifyTime;
}

