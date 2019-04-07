package us.betahouse.toptensinger.dao;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.betahouse.toptensinger.model.Player;
import us.betahouse.toptensinger.model.builder.PlayerBuilder;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void test(){
        PlayerBuilder playerBuilder=PlayerBuilder.aPlayer();
        List<Player> playerList=new ArrayList<>(256);
        playerBuilder.withContestId(1L);
        for(long i=1;i<123;i++){
            playerBuilder.withPlayerId(i)
                    .withName("jk"+i);
            playerList.add(playerBuilder.build());
        }
        playerBuilder.withContestId(2L);
        for(long i=1;i<20;i++){
            playerBuilder.withPlayerId(i)
                    .withName("t"+i);
            playerList.add(playerBuilder.build());
        }
        playerBuilder.withContestId(3L);
        for(long i=1;i<10;i++){
            playerBuilder.withPlayerId(i)
                    .withName("s"+i);
            playerList.add(playerBuilder.build());
        }
        playerRepository.saveAll(playerList);
    }

    @Test
    public void test1(){
        Player player=playerRepository.findById(1L).get();
        System.out.println(JSON.toJSONString(player));
        System.out.println(player.getScoreList().get(1).getPlayer());
//        System.out.println(player);
    }
    @Test
    public void test2(){
        List<Player> playerList=playerRepository.findByContestId(1L);
        playerList.forEach(player -> player.setNumber(player.getScoreList().size()));
        System.out.println(playerList);
    }

}