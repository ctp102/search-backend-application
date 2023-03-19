package io.search.core.search.domain;

import io.search.core.commons.domain.BaseTimeEntity;
import io.search.core.search.enums.PlatformType;
import lombok.*;

import javax.persistence.*;

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

    @Enumerated(EnumType.STRING)
    private PlatformType platform;
    private long count;

    public SearchDomain(String query, PlatformType platform) {
        this(query, platform, 1);
    }

    public SearchDomain(String query, PlatformType platform, long count) {
        this.query = query;
        this.platform = platform;
        this.count = count;
    }

    public void increaseCount() {
        this.count += 1;
    }

}
