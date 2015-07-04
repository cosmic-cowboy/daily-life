package com.slgerkamp.daily.life.core.domain;

import com.slgerkamp.daily.life.core.domain.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>日記のエントリーを管理するリポジトリです。
 *
 * JpaRepositoryを継承した時点で、下記が実装される
 * findOne
 * findAll
 * save
 * delete
 *
 */
public interface EntryRepository extends JpaRepository<Entry, Long> {

}
