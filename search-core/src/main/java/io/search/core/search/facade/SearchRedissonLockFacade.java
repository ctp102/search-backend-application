package io.search.core.search.facade;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.form.SearchForm;
import io.search.core.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchRedissonLockFacade {

    private final RedissonClient redissonClient;
    private final SearchService searchService;

    /**
     * 블로그 검색
     *
     * @param searchForm
     * @param pagingForm
     */
    public void searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        RLock lock = redissonClient.getLock(String.format("search:%s", searchForm.getQuery()));
        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);
            if (!available) {
                log.error("lock 획득 실패 (timeout)");
                return;
            }

            searchService.searchBlog(searchForm, pagingForm);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
