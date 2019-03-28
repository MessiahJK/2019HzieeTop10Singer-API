/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.model.builder;

import us.betahouse.toptensinger.model.Dictionary;

/**
 * @author MessiahJK
 * @version : DictionaryBuilder.java 2019/03/24 22:07 MessiahJK
 */
public final class DictionaryBuilder {
    private Long id;
    private String key;
    private String value;
    private String desc;
    private String remark;

    private DictionaryBuilder() {
    }

    public static DictionaryBuilder aDictionary() {
        return new DictionaryBuilder();
    }

    public DictionaryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DictionaryBuilder withKey(String key) {
        this.key = key;
        return this;
    }

    public DictionaryBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public DictionaryBuilder withDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public DictionaryBuilder withRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Dictionary build() {
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        dictionary.setKey(key);
        dictionary.setValue(value);
        dictionary.setDescription(desc);
        dictionary.setRemark(remark);
        return dictionary;
    }
}
