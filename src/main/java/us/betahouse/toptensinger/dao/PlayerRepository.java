/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Player;

import java.util.List;

/**
 * 选手存储仓库
 *
 * @author MessiahJK
 * @version : PlayerRepository.java 2019/03/26 15:35 MessiahJK
 */
public interface PlayerRepository extends JpaRepository<Player,Long> {
    /**
     * 通过比赛id查找选手列表
     *
     * @param contestId 比赛id
     * @return 选手实体列表
     */
    List<Player> findByContestId(Long contestId);


}
