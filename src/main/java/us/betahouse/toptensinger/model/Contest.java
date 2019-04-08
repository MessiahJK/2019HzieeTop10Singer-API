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
import java.util.Date;

/**
 * @author MessiahJK
 * @version : Contest.java 2019/03/25 19:41 MessiahJK
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Contest {
    /**
     * 比赛id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 比赛名
     */
    private String name;

    /**
     * 状态
     */
    private String status;

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

    public Contest(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public Contest() {
    }
}
