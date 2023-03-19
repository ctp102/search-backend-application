package io.search.api.utils;

import io.search.api.CommonTest;
import io.search.core.commons.form.PagingForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThymeleafUtilsTest extends CommonTest {

    @Autowired
    private ThymeleafUtils thymeleafUtils;

    @Test
    public void getRowNum() throws Exception {
        PagingForm pagingForm = new PagingForm();
        pagingForm.setPage(2);
        int index = 5;
        int rowNum = thymeleafUtils.getRowNum(pagingForm, index);
        viewJson(rowNum);
    }

}