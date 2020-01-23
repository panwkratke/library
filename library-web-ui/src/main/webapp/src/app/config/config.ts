export class RestApiUrlConfig {
  static REST_API_URL = 'http://localhost:8080/';
  static REST_API_USER_REGISTER = RestApiUrlConfig.REST_API_URL + 'users/register';
  static REST_API_USER_GET_LIST = RestApiUrlConfig.REST_API_URL + 'users/list';
  static REST_API_USER_GET_BY_ID = RestApiUrlConfig.REST_API_URL + 'users/';
  static REST_API_USER_GET_BY_USERNAME = RestApiUrlConfig.REST_API_URL + 'users/username/';
  static REST_API_USER_DELETE_BY_ID = RestApiUrlConfig.REST_API_URL + 'users/';
  static REST_API_USER_UPDATE_BY_ID = RestApiUrlConfig.REST_API_URL + 'users';
  static REST_API_USER_LOGIN = RestApiUrlConfig.REST_API_URL + 'auth/login';
  static REST_API_USER_LOGOUT = RestApiUrlConfig.REST_API_URL + 'auth/logout';

  static REST_API_BOOK_SAVE = RestApiUrlConfig.REST_API_URL + 'books';
  static REST_API_BOOK_GET_BY_ID = RestApiUrlConfig.REST_API_URL + 'books/';
  static REST_API_BOOK_GET_BY_TITLE_AND_AUTHOR = RestApiUrlConfig.REST_API_URL + 'books/title/author';
  static REST_API_BOOK_GET_LIST = RestApiUrlConfig.REST_API_URL + 'books/list';
  static REST_API_BOOK_GET_LIST_BY_AUTHOR = RestApiUrlConfig.REST_API_URL + 'books/list/author/';
  static REST_API_BOOK_GET_LIST_BY_GENRE = RestApiUrlConfig.REST_API_URL + 'books/list/genre/';
  static REST_API_BOOK_GET_LIST_BY_TITLE = RestApiUrlConfig.REST_API_URL + 'books/list/title/';
  static REST_API_BOOK_DELETE_BY_ID = RestApiUrlConfig.REST_API_URL + 'books/';
  static REST_API_BOOK_UPDATE_BY_ID = RestApiUrlConfig.REST_API_URL + 'books';

  static REST_API_BORROW_ORDER_CREATE = RestApiUrlConfig.REST_API_URL + 'orders';
  static REST_API_BORROW_ORDER_GET_LIST = RestApiUrlConfig.REST_API_URL + 'orders/list';
  static REST_API_BORROW_ORDER_GET_LIST_BY_USER_ID = RestApiUrlConfig.REST_API_URL + 'orders/list/user/';
  static REST_API_BORROW_ORDER_GET_LIST_BY_BOOK_ID = RestApiUrlConfig.REST_API_URL + 'orders/list/book/';
  static REST_API_BORROW_ORDER_GET_BY_ID = RestApiUrlConfig.REST_API_URL + 'orders/';
  static REST_API_BORROW_ORDER_RETURN = RestApiUrlConfig.REST_API_URL + 'orders/';

  static REST_API_DICTIONARY_GET_BY_DICTIONARY_NAME = RestApiUrlConfig.REST_API_URL + 'dictionaries/';
}
