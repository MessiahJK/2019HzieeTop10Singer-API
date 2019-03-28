/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author MessiahJK
 * @version : dictionary.java 2019/03/24 21:05 MessiahJK
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Dictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 键
     */
    @Column(name = "t_key")
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create", nullable = false)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified", nullable = false)
    private Date gmtModified;

    public Dictionary(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Dictionary(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public Dictionary() {
    }
}
