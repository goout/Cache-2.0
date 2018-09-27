package com.taraskin.cache.controller;

import com.taraskin.cache.entity.Record;
import com.taraskin.cache.exceptions.RecordNotFoundException;
import com.taraskin.cache.repository.RecordRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping
public class RecordController {

    private static final Logger LOG = Logger.getLogger(RecordController.class.getName());

    private final RecordRepo recordRepo;
    private final Cache caffeineCache;

    @Autowired
    public RecordController(RecordRepo recordRepo, CacheManager cacheManager) {
        this.recordRepo = recordRepo;
        this.caffeineCache = cacheManager.getCache("records");
    }


    @GetMapping("/{key}")
    public Record getRecordByKey(@PathVariable("key") Record record) {

        if (record != null) {

            Record recordFromCache = caffeineCache.get(record.getKey(), Record.class);

            if (recordFromCache != null) {
                LOG.info("record from cache!");
                return recordFromCache;
            } else {
                LOG.info("record from db!");
                caffeineCache.put(record.getKey(), record);
                return record;
            }
        } else {
            throw new RecordNotFoundException();
        }
    }


    @PostMapping
    public Record createOrUpdateRecord(@RequestBody Record record) {

        if (recordRepo.existsById(record.getKey())) {
            evictRecordFromCache(record.getKey());
            Record recordFromDB = recordRepo.getOne(record.getKey());
            BeanUtils.copyProperties(record, recordFromDB);
            return recordRepo.save(recordFromDB);
        }

        return recordRepo.save(record);
    }


    public void evictRecordFromCache(Long key) {
        caffeineCache.evict(key);
    }

}
