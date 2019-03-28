/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Player;

import java.util.List;

/**
 * @author MessiahJK
 * @version : PlayerRepository.java 2019/03/26 15:35 MessiahJK
 */
public interface PlayerRepository extends JpaRepository<Player,Long> {
    /**
     * 通过比赛id查找选手列表
     *
     * @param contestId
     * @return
     */
    List<Player> findByContestId(Long contestId);


}
