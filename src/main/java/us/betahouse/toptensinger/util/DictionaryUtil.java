/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.betahouse.toptensinger.constant.DictionaryKeys;
import us.betahouse.toptensinger.dao.DictionaryRepository;
import us.betahouse.toptensinger.model.Dictionary;

import java.math.BigDecimal;

/**
 * @author MessiahJK
 * @version : DictionaryUtil.java 2019/03/26 0:28 MessiahJK
 */
@Component
public class DictionaryUtil {

    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryUtil(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public  Long getActiveContest(){
        return Long.valueOf(dictionaryRepository.findByKey(DictionaryKeys.ACTIVE_CONTEST).getValue());
    }

    public void updateActiveContest(Long value,String description){
        Dictionary dictionary=dictionaryRepository.findByKey(DictionaryKeys.ACTIVE_CONTEST);
        dictionary.setValue(String.valueOf(value));
        dictionary.setDescription(description);
        dictionaryRepository.save(dictionary);
    }

    public BigDecimal getStudentFractionRatio(){
        return BigDecimal.valueOf(Double.valueOf(dictionaryRepository.findByKey(DictionaryKeys.STUDENT_FRACTION_RATIO).getValue()));
    }

    public BigDecimal getTeacherFractionRatio(){
        return BigDecimal.valueOf(Double.valueOf(dictionaryRepository.findByKey(DictionaryKeys.TEACHER_FRACTION_RATIO).getValue()));
    }
}
