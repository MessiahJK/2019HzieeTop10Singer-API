/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 选手
 *
 * @author MessiahJK
 * @version : Player.java 2019/03/26 0:50 MessiahJK
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Player {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 选手id 按场次
     */
    private Long playerId;

    /**
     * 选手名
     */
    private String name;

    /**
     * 选手照片
     */
    private String image;

    /**
     * 选手信息
     */
    @Lob
    @Column(columnDefinition="text")
    private String message;

    /**
     * 比赛
     */
    private Long contestId;

    /**
     * 平均分
     */
    @Transient
    private BigDecimal averageScore;

    /**
     * 分数个数
     */
    @Transient
    private Integer number;
    /**
     * 创建时间
     */
   @JsonIgnore
    @CreatedDate
    @Column(name = "gmt_create", nullable = false)
    private Date gmtCreate;

    /**
     * 修改时间
     */
   @JsonIgnore
    @LastModifiedDate
    @Column(name = "gmt_modified", nullable = false)
    private Date gmtModified;

    /**
     * 分数列表
     */
    @JsonIgnore
    @OneToMany(mappedBy = "player",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<Score> scoreList;

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", message='" + message + '\'' +
                ", contestId=" + contestId +
                ", averageScore=" + averageScore +
                ", number=" + number +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
