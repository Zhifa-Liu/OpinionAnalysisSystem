package cn.edu.neu.tool.hanlp;

import cn.edu.neu.tool.NlpUtil;
import cn.edu.neu.tool.hanlp.ClassifierConstant;
import com.hankcs.hanlp.classification.classifiers.AbstractClassifier;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.classification.corpus.FileDataSet;
import com.hankcs.hanlp.classification.corpus.IDataSet;
import com.hankcs.hanlp.classification.models.AbstractModel;
import com.hankcs.hanlp.classification.models.NaiveBayesModel;
import com.hankcs.hanlp.classification.tokenizers.HanLPTokenizer;

import java.io.*;
import java.util.Map;

/**
 * @author 32098
 */
public class HanLpClassifier {
    private static AbstractClassifier classifier = null;

    /**
     * 初始化分类器
     */
    private static void initClassifier(){
        AbstractModel model = loadModel(ClassifierConstant.MODEL_PATH);
        if(model==null){
            System.out.println("No model find, begin train model!");
            IDataSet dataSet = null;
            try {
                System.out.println(ClassifierConstant.TRAIN_DATASET_PATH);
                BufferedReader reader = new BufferedReader(new FileReader(ClassifierConstant.TRAIN_DATASET_PATH));
                String str;
                dataSet = new FileDataSet().setTokenizer(new HanLPTokenizer());
                System.out.println("Prepare dataset!");
                // ignore first line
                str = reader.readLine();
                while ((str=reader.readLine())!=null){
                    dataSet.add(str.substring(0,1), str.substring(2));
                }
                System.out.println("Dataset prepared!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            classifier = new NaiveBayesClassifier();
            classifier.train(dataSet);
            model = classifier.getModel();
            saveModel(ClassifierConstant.MODEL_PATH, model);
        }else{
            System.out.println("Model init succeeded!");
            classifier = new NaiveBayesClassifier((NaiveBayesModel) model);
        }
    }

    /**
     * 保存模型：序列化
     * @param modelPath 模型路径
     * @param model 模型
     */
    private static void saveModel(String modelPath, AbstractModel model){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelPath))) {
            oos.writeObject(model);
            System.out.println("Save Model Succeeded!");
        } catch (Exception e) {
            System.err.println("Save Model Failed!");
            System.err.println(e.getMessage());
        }
    }

    /**
     * 载入模型：反序列化
     * @param modelPath 模型路径
     * @return 模型
     */
    private static AbstractModel loadModel(String modelPath){
        InputStream inputStream = HanLpClassifier.class.getResourceAsStream(modelPath);
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            Object o = ois.readObject();
            return (AbstractModel) o;
        } catch (FileNotFoundException e) {
            System.err.println("Load Model Failed(Model file：" + modelPath+" not Found!)");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param sentence 句子文本
     * @return 属于正面的概率-属于负面的概率
     */
    public static Double getScore(String sentence) {
        if(classifier==null){
            initClassifier();
        }
        Map<String, Double> map = classifier.predict(sentence);
        return map.get("1")-map.get("0");
    }

    /**
     * For test
     * @param args args
     */
    public static void main(String[] args) {
        System.out.println(getScore("天安门"));
        System.out.println(getScore("哇哦今年的春夏季衣服不错诶"));
        System.out.println(getScore("去死吧"));
        System.out.println(getScore("加油"));
        System.out.println(getScore("祝福伟大的党伟大的祖国"));
    }
}


