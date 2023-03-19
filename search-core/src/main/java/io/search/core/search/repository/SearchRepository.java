package io.search.core.search.repository;

import io.search.core.search.domain.SearchDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<SearchDomain, Long> {

}
