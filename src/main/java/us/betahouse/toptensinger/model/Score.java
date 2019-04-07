/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author MessiahJK
 * @version : Score.java 2019/03/26 11:59 MessiahJK
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Score {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分数
     */
    private BigDecimal value;


    /**
     * 比赛场次
     */
    private Long contestId;

    /**
     * 类型
     */
    private String type;

    /**
     * 对应的选手
     */
    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="player_id")
    private Player player;

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

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", value=" + value +
                ", contestId=" + contestId +
                ", type='" + type + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }


}
