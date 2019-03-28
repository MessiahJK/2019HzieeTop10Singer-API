package us.betahouse.toptensinger.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.betahouse.toptensinger.model.Dictionary;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DictionaryRepositoryTest {

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Test
    public void create(){
        Dictionary dictionary=new Dictionary();
        dictionary.setKey("activeContest");
        dictionary.setValue("1");
        dictionary.setDescription("目前比赛");
        dictionaryRepository.save(dictionary);
    }

}