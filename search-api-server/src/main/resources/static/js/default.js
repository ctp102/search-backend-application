const searchKeyword = () => {
    // 여기에 query, sort 값을 가져온다
    $('#search-form').attr('action', location.pathname).submit();
}