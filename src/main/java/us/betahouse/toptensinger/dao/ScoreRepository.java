/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Score;

import java.util.List;

/**
 * 分数存储仓库
 *
 * @author MessiahJK
 * @version : ScoreRepository.java 2019/03/26 15:43 MessiahJK
 */
public interface ScoreRepository extends JpaRepository<Score,Long> {

    /**
     * 通过比赛id和选手id查找
     *
     * @param contestId 比赛id
     * @param playerId  选手id
     * @return 分数列表
     */
    List<Score> findByContestIdAndPlayerId(Long contestId,Long playerId);

    /**
     * 通过比赛id、选手id、类型查找
     *
     * @param contestId 比赛id
     * @param playerId 选手id
     * @param type 类型
     * @return 分数列表
     */
    List<Score> findByContestIdAndPlayerIdAndType(Long contestId,Long playerId,String type);
}
