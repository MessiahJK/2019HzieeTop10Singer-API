/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.betahouse.toptensinger.constant.ContestStatus;
import us.betahouse.toptensinger.constant.DictionaryKeys;
import us.betahouse.toptensinger.dao.ContestRepository;
import us.betahouse.toptensinger.dao.DictionaryRepository;
import us.betahouse.toptensinger.model.Contest;
import us.betahouse.toptensinger.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MessiahJK
 * @version : CommonService.java 2019/03/25 19:52 MessiahJK
 */
@Service
public class CommonService {

    private final ContestRepository contestRepository;
    private final DictionaryRepository dictionaryRepository;

    @Autowired
    public CommonService(ContestRepository contestRepository, DictionaryRepository dictionaryRepository) {
        this.contestRepository = contestRepository;
        this.dictionaryRepository = dictionaryRepository;
    }


    public void init(){
        List<Contest> contestList=new ArrayList<>(8);
        contestList.add(new Contest("海选", ContestStatus.ACTIVE));
        contestList.add(new Contest("复赛", ContestStatus.SLEEP));
        contestList.add(new Contest("决赛10进5", ContestStatus.SLEEP));
        contestList.add(new Contest("决赛5进3", ContestStatus.SLEEP));
        contestList.add(new Contest("总决赛", ContestStatus.SLEEP));
        for(Contest contest:contestList){
            if(contestRepository.findByName(contest.getName())==null){
                contestRepository.save(contest);
            }
        }
        List<Dictionary> dictionaryList=new ArrayList<>(16);
        dictionaryList.add(new Dictionary(DictionaryKeys.ACTIVE_CONTEST,"1" ,"海选" ));
        dictionaryList.add(new Dictionary(DictionaryKeys.STUDENT_FRACTION_RATIO, "0.3", "学生分数比例"));
        dictionaryList.add(new Dictionary(DictionaryKeys.TEACHER_FRACTION_RATIO, "0.7", "教师分数比例"));
        for(Dictionary dictionary:dictionaryList){
            if(dictionaryRepository.findByKey(dictionary.getKey())==null){
                dictionaryRepository.save(dictionary);
            }
        }
    }
}
