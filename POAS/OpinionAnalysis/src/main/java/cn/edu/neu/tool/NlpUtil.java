package cn.edu.neu.tool;

import cn.edu.neu.tool.hanlp.HanLpClassifier;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superwasabi
 *
 **/
public class NlpUtil {
    private static final List<String> STOP_WORDS = new ArrayList<>();

    static {
        try {
            // String filePath = System.getProperty("user.dir")+"/OpinionAnalysis/src/main/java/cn/edu/neu/tool/stopwords.txt".replace("/", File.separator);
            // String filepath = NlpUtil.class.getResource("/stopwords.txt").getPath();
            // filepath = URLDecoder.decode(filepath, "UTF-8");
            // System.out.println(filepath);
            // BufferedReader reader = new BufferedReader(new FileReader(filepath));

            InputStream inputStream = NlpUtil.class.getResourceAsStream("/stopwords.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            STOP_WORDS.add(" ");
            STOP_WORDS.add("  ");
            while ((str=reader.readLine())!=null){
                STOP_WORDS.add(str.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 情感分类
     * @param text text
     * @return emotion：1-> positive; 0->negative; 2->neutral
     */
    public static String textEmotion(String text){
        double posMinuteNeg = HanLpClassifier.getScore(text);
        if(posMinuteNeg>0.4){
            return "1";
        }else if(posMinuteNeg<-0.4){
            return "0";
        }else{
            return "2";
        }
    }

    /**
     * 文本分割
     * @param text 文本
     * @return 分割结果：中文词 List
     */
    public static List<String> segmentSentence(String text){
        String newText = text
                .replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "")
                .replaceAll("[\\p{S}]", "")
                .replaceAll("[\\p{N}]", "");
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        List<String> outs = new ArrayList<>();
        for (String s: jiebaSegmenter.sentenceProcess(newText)) {
            if(!STOP_WORDS.contains(s) && s.length()!=1){
                outs.add(s);
            }
        }
        return outs;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(textEmotion("你真好"));

        // System.out.println(STOP_WORDS);

        List<String> outs = segmentSentence("【看懂上网打官司，#人民法院在线诉讼规则#来了】在线法庭，你体验过吗？数据显示，2020年1月1日至2021年5月31日，全国法院在线开庭128.8万次。为推进和规范在线诉讼活动，最高人民法院17日发布《人民法院在线诉讼规则》，明确在线诉讼“合法自愿”原则，强调#在线庭审不得随意录音录像#等。看大图，为你划重点");
        System.out.println(outs);
        outs = segmentSentence("【#2020成都城镇单位平均工资83556元#】据成都市统计局消息，2020年成都市城镇全部单位就业人员平均工资为83556元，比上年增加5664元，同比增长7.3%，扣除物价因素，实际增长4.7%。城镇非私营单位就业人员平均工资为101995元，比上年增加7273元，同比增长7.7%；城镇私营单位就业人员平均工资为58023元，比上年增加7328元，同比增长14.5%。");
        System.out.println(outs);
    }
}
