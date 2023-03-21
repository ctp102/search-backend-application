package io.search.core.search.form;

import io.search.core.search.enums.PlatformType;
import io.search.core.search.enums.SortType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchForm {

    private String query;
    private String sort = SortType.ACCURACY.name().toLowerCase(); // default: accuracy

    public SearchForm(String query) {
        this.query = query;
    }

    /**
     * 플랫폼에 따른 정렬 타입 변경
     *
     * @param platformType
     */
    public void changeSortType(PlatformType platformType) {

        if (platformType == PlatformType.KAKAO) {
            if (this.sort.equals(SortType.ACCURACY.name().toLowerCase())) {
                this.sort = "accuracy";
                return;
            }

            if (this.sort.equals(SortType.RECENCY.name().toLowerCase())) {
                this.sort = "recency";
            }
        }

        if (platformType == PlatformType.NAVER) {
            if (this.sort.equals(SortType.ACCURACY.name().toLowerCase())) {
                this.sort = "sim";
                return;
            }

            if (this.sort.equals(SortType.RECENCY.name().toLowerCase())) {
                this.sort = "date";
            }
        }
    }

}
