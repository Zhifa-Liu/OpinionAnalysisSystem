package cn.edu.neu.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 32098
 */
public class EventGetUtil {
    private static final Pattern EVENT_GET_PATTERN = Pattern.compile("#[^#]+#");

    public static List<String> getArticleEvents(String originalString){
        List<String> hashtagSet=new ArrayList<String>();
        Matcher matcher = EVENT_GET_PATTERN.matcher(originalString);
        while (matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String tmpHashtag=originalString.substring(matchStart+1,matchEnd-1);
            if (tmpHashtag.length()>2){
                hashtagSet.add(tmpHashtag.trim());
            }
            originalString=originalString.replace("#"+tmpHashtag+"#","");
            matcher = EVENT_GET_PATTERN.matcher(originalString);
        }
        return hashtagSet;
    }

    public static void main(String[] args) {
        String s="【看懂上网打官司，#人民法院在线诉讼规则#来了】在线法庭，你体验过吗？数据显示，2020年1月1日至2021年5月31日，全国法院在线开庭128.8万次。为推进和规范在线诉讼活动，最高人民法院17日发布《人民法院在线诉讼规则》，明确在线诉讼“合法自愿”原则，强调#在线庭审不得随意录音录像#等。看大图，为你划重点";
        for(String str: getArticleEvents(s)){
            System.out.println(str);
        }
        s = "欧洲杯德国队搏命式全员进攻打懵葡萄牙勒夫最后一招：零后卫#欧洲杯##德国葡萄牙#北京时间年月日凌晨在欧洲杯小组赛中德国队逆转战胜葡萄牙取得了关键性的胜利本场比赛德国队首发阵容与对阵法国时完全一样不过开场后的战术安排完全不同德国队先发制人不仅中前场快速推进而且后卫线也几乎全部压上释放出专注进攻的信号左右两翼的戈森斯和基米希异常活跃屡屡策动攻势哈弗茨穆勒格纳布里的中路衔接也很频繁中后卫胡梅尔斯吕迪格金特尔基本都在对方半场游弋有时还进入葡萄牙队禁区开场分钟德国队次射门次射正尽管如此葡萄牙第一次反击就攻破了德国队球门除了吕迪格外德国其他后卫都没能回防到位然而德国队的强攻战术没有丝毫改变继续形成高位逼抢比分领先的葡萄牙显然很不适应对手近乎疯狂的禁区冲击竟然两次自摆乌龙下半场德国队适当放慢了进攻速度但由守转攻的节奏控制仍然发挥了功效哈弗茨戈森斯先后破门德国队几乎搏命式的打法（特别是上半场）已经很久未见主教练勒夫以攻代守冒险出击得到了不可思议的胜利与球星云集人员齐整士气正盛的葡萄牙队对阵德国人踢出了久违的攻势足球全场次射门次射正个进球这是勒夫的自我救赎也是德国队的自我突破德国队的新打法之所以是以攻代守主要是表现在两个方面：一方面进攻防守球员同时压过半场造成局部攻击群人数的优势给对手造成很大的心理压力从而掌握比赛的主动权；另一方面中前场球员坚决执行就地反抢的战术安排减轻了后防线的部分压力当然全员进攻不等于全攻全守德国队现有的后卫队员很难支撑前后场大范围跑动即使站桩防守也没有零封对手的把握总体看勒夫的新战术有侧重地解决进攻无效低效的顽疾甚至不惜自我削弱后场防守强度集中优势兵力与对手拼刺刀一定要进球这个战术思路对于擅长防守反击的球队能否奏效还要看实战结果由于本场比赛前法国被匈牙利逼平法国积分暂时排在小组第一德国队分与法国的距离没有拉开暂时度过危机保留了小组出现的可能性";
        for(String str: getArticleEvents(s)){
            System.out.println(str);
        }
        s = "#欧洲杯# 这次欧洲杯6个小组中唯一没有球队跻身8强的就是“死亡小组”..........如果他们三队进入16强的身份换成是德国队第一葡萄牙队第二法国队第三，命运会不一样么？";
        System.out.println(getArticleEvents(s));
        s = "#谭松韵演的王会悟 #演的很好啊，演技在线，致敬先辈";
        System.out.println(getArticleEvents(s));
    }
}
