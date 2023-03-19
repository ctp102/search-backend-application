package io.search.core.commons.form;

import lombok.Data;

@Data
public class PagingForm implements Paging {

    private int page = 1;
    private int size = 10;
    private long totalCount;

}
