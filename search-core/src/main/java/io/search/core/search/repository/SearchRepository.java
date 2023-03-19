package io.search.core.search.repository;

import io.search.core.search.domain.SearchDomain;
import io.search.core.search.enums.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<SearchDomain, Long> {

    @Query("SELECT s FROM SearchDomain s ORDER BY s.count DESC")
    List<SearchDomain> findTop10HotSearch();

    SearchDomain findSearchByQueryAndPlatform(
            @Param("query") String query,
            @Param("platform") PlatformType platform
    );

}
