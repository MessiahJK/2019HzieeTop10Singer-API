package us.betahouse.toptensinger.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.betahouse.toptensinger.model.Player;
import us.betahouse.toptensinger.model.builder.PlayerBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


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

}