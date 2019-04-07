/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model.builder;

import us.betahouse.toptensinger.model.Player;

/**
 * @author MessiahJK
 * @version : PlayerBuilder.java 2019/04/07 23:34 MessiahJK
 */
public final class PlayerBuilder {
    private Long playerId;
    private String name;
    private String image;
    private String message;
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

    public PlayerBuilder withImage(String image) {
        this.image = image;
        return this;
    }

    public PlayerBuilder withMessage(String message) {
        this.message = message;
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
        player.setImage(image);
        player.setMessage(message);
        player.setContestId(contestId);
        return player;
    }
}
