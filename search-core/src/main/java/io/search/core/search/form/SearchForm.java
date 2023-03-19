package io.search.core.search.form;

import io.search.core.search.enums.SortType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchForm {

    private String query;
    private String sort = SortType.ACCURACY.name().toLowerCase(); // [ recency(default), accuracy ]

}
