package io.search.core.search.domain;

import io.search.core.commons.domain.BaseTimeEntity;
import io.search.core.search.enums.PlatformType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "query", "platform", "count"})
public class SearchDomain extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "search_id")
    private Long id;

    private String query;
    private PlatformType platform;
    private long count;

}
