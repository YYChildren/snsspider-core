package com.mingchao.snsspider.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="t_user_key")
public class UserKey extends HadoopString{
	private Long id;
	private Long qq;
	private Boolean visitable;
	private String desc;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getQq() {
		return qq;
	}
	public void setQq(Long qq) {
		this.qq = qq;
	}
	
	@Override
	public String toHadoopString() {
		return  convertNull(id) + "\t" + convertNull(qq) ;
	}
	
	public Boolean getVisitable() {
		return visitable;
	}
	public void setVisitable(Boolean visitable) {
		this.visitable = visitable;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}