package io.search.core.util;

import com.google.common.collect.ImmutableMap;
import io.search.core.CommonTest;
import io.search.core.search.response.kakao.KakaoBlogSearchResponse;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.restclient.kakao.KakaoRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class KakaoRestClientTest extends CommonTest {

    @Autowired
    private KakaoRestClient kakaoRestClient;

    @Test
    public void blogSearch() throws Exception {
        String query = "테스트";
//        String sort = "accuracy";
        String sort = "recency";
        int page = 1;
        int size = 20;
        SearchResponse<KakaoBlogSearchResponse> response = kakaoRestClient.getBlogSearch(
                ImmutableMap.of(
                        "query", query,
                        "sort", sort,
                        "page", page,
                        "size", size
                )
        );

        viewJson(response);
    }

}