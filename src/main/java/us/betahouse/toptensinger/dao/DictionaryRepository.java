/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import us.betahouse.toptensinger.model.Dictionary;

/**
 * 字典存储仓库
 *
 * @author MessiahJK
 * @version : DictionaryRepository.java 2019/03/24 22:02 MessiahJK
 */
public interface DictionaryRepository extends JpaRepository<Dictionary,Long> {

    /**
     * 通过key查询
     *
     * @param key key值
     * @return 字典实体
     */
    Dictionary findByKey(String key);

}
