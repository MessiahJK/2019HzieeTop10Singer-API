/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MessiahJK
 * @version : CommonPlayer.java 2019/03/26 21:42 MessiahJK
 */
@Data
public class CommonPlayer {
    private Long id;

    private Long playerId;

    private String name;

    private Long contestId;

    private BigDecimal averageScore;

    private Integer number;
}
