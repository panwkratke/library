export class ApiResponse<T> {
  httpError: number;
  apiError: number;
  apiErrorMsg: string;
  content: T;
}
