/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Contest;

import java.util.List;

/**
 * @author MessiahJK
 * @version : ContestRepository.java 2019/03/25 19:47 MessiahJK
 */
public interface ContestRepository extends JpaRepository<Contest,Long> {

    /**
     * 通过比赛名查找
     *
     * @param name
     * @return
     */
    Contest findByName(String name);
}
