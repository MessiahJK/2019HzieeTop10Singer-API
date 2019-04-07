/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import lombok.Data;

import java.util.List;

/**
 * 分数结果集
 *
 * @author MessiahJK
 * @version : ScoreResult.java 2019/04/03 8:39 MessiahJK
 */
@Data
public class ScoreResult {

    /**
     * 学生分数
     */
    List<Score> studentScore;

    /**
     * 教师分数
     */
    List<Score> teacherScore;

    public ScoreResult(List<Score> studentScore, List<Score> teacherScore) {
        this.studentScore = studentScore;
        this.teacherScore = teacherScore;
    }

    public ScoreResult() {
    }
}
