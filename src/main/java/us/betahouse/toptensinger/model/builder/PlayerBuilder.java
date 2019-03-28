/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model.builder;

import us.betahouse.toptensinger.model.Player;

/**
 * @author MessiahJK
 * @version : PlayerBuilder.java 2019/03/27 11:08 MessiahJK
 */
public final class PlayerBuilder {
    private Long playerId;
    private String name;
    private Long contestId;

    private PlayerBuilder() {
    }

    public static PlayerBuilder aPlayer() {
        return new PlayerBuilder();
    }

    public PlayerBuilder withPlayerId(Long playerId) {
        this.playerId = playerId;
        return this;
    }

    public PlayerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder withContestId(Long contestId) {
        this.contestId = contestId;
        return this;
    }

    public Player build() {
        Player player = new Player();
        player.setPlayerId(playerId);
        player.setName(name);
        player.setContestId(contestId);
        return player;
    }
}
