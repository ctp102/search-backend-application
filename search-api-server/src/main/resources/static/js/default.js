const searchQuery = () => {
    $('#search-form').attr('action', location.pathname).submit();
}