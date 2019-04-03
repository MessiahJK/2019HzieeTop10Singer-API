/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import lombok.Data;

import java.util.List;

/**
 * @author MessiahJK
 * @version : ScoreResult.java 2019/04/03 8:39 MessiahJK
 */
@Data
public class ScoreResult {

    List<Score> studentScore;

    List<Score> teacherScore;

    public ScoreResult(List<Score> studentScore, List<Score> teacherScore) {
        this.studentScore = studentScore;
        this.teacherScore = teacherScore;
    }

    public ScoreResult() {
    }
}
