package com.slgerkamp.daily.life.core.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slgerkamp.daily.life.core.domain.entity.Entry;

/**
 * <p>日記のエントリーを司るクラスです。
 *
 */
@Service
public class EntryService {

	@Autowired
	EntryRepository entryRepository;

	public List<Entry> findAll() {
		return entryRepository.findAll();
	}

	public Entry create(Entry customer) {
		return entryRepository.save(customer);
	}

	public void delete(Long id) {
		entryRepository.delete(id);
	}
}
