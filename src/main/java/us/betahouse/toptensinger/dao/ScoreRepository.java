/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Score;

import java.util.List;

/**
 * @author MessiahJK
 * @version : ScoreRepository.java 2019/03/26 15:43 MessiahJK
 */
public interface ScoreRepository extends JpaRepository<Score,Long> {

    /**
     * 通过比赛id和使用者id查找
     *
     * @param contestId
     * @param playerId
     * @return
     */
    List<Score> findByContestIdAndPlayerId(Long contestId,Long playerId);

    /**
     * 通过比赛id、使用者id、类型查找
     *
     * @param contestId
     * @param playerId
     * @param type
     * @return
     */
    List<Score> findByContestIdAndPlayerIdAndType(Long contestId,Long playerId,String type);
}
