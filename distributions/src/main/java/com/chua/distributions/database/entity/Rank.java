package com.chua.distributions.database.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.chua.distributions.database.entity.base.BaseEntity;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	21 Feb 2017
 */
@MappedSuperclass
public class Rank extends BaseEntity {

	private static final long serialVersionUID = 5477448175554599685L;

	private Integer rank;
	
	private Integer previousRank;
	
	private Date lastInclusiveDate;

	@Basic
	@Column(name = "rank")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Basic
	@Column(name = "previous_rank")
	public Integer getPreviousRank() {
		return previousRank;
	}

	public void setPreviousRank(Integer previousRank) {
		this.previousRank = previousRank;
	}

	@Version
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_inclusive_date", nullable = false)
	public Date getLastInclusiveDate() {
		return lastInclusiveDate;
	}

	public void setLastInclusiveDate(Date lastInclusiveDate) {
		this.lastInclusiveDate = lastInclusiveDate;
	}
}
