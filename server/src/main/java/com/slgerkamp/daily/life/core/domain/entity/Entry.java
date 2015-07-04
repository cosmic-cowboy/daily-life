package com.slgerkamp.daily.life.core.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>日記のエントリーです。
 *
 */
@Entity
@Table(name = "entry")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Entry {

	@SuppressWarnings("unused")
	private Long userId;

	@Id
	private Long messageId;

	@SuppressWarnings("unused")
	private String messageType;

	@SuppressWarnings("unused")
	private String content;

	@SuppressWarnings("unused")
	private Timestamp postDate;

	@SuppressWarnings("unused")
	private Timestamp createDate;

	@SuppressWarnings("unused")
	private Timestamp updateDate;

}
