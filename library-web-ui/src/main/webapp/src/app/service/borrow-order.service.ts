import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { BorrowOrder } from '../model/borrowOrder';
import { HttpClient } from '@angular/common/http';
import { CreateBorrowOrderDto } from '../model/createBorrowOrderDto';
import { ApiResponse } from '../model/apiResponse';
import { RestApiUrlConfig } from '../config/config';

@Injectable({
  providedIn: 'root'
})
export class BorrowOrderService {

  $createborrowOrder: Observable<BorrowOrder>;
  private createborrowOrder: Subject<BorrowOrder> = new Subject();

  constructor(private http: HttpClient) {
    this.$createborrowOrder = this.createborrowOrder.asObservable();
  }

  public onCreateBorrowOrder(borrowOrder: BorrowOrder) {
    this.createborrowOrder.next(borrowOrder);
  }

  public createBorrowOrder(createBorrowOrderDto: CreateBorrowOrderDto): Observable<ApiResponse<BorrowOrder>> {
    return this.http.post<ApiResponse<any>>(RestApiUrlConfig.REST_API_BORROW_ORDER_CREATE, createBorrowOrderDto);
  }

  public returnBorrowOrder(id: number): Observable<ApiResponse<BorrowOrder>> {
    return this.http.delete<ApiResponse<BorrowOrder>>(RestApiUrlConfig.REST_API_BORROW_ORDER_RETURN + id);
  }

  public getBorrowOrders(): Observable<Array<BorrowOrder>> {
    return this.http.get<Array<BorrowOrder>>(RestApiUrlConfig.REST_API_BORROW_ORDER_GET_LIST);
  }

  public getBorrowOrdersByUserId(id: number): Observable<Array<BorrowOrder>> {
    return this.http.get<Array<BorrowOrder>>(RestApiUrlConfig.REST_API_BORROW_ORDER_GET_LIST_BY_USER_ID + id);
  }

  public getBorrowOrdersByBookId(id: number): Observable<Array<BorrowOrder>> {
    return this.http.get<Array<BorrowOrder>>(RestApiUrlConfig.REST_API_BORROW_ORDER_GET_LIST_BY_BOOK_ID + id);
  }

  public getBorrowOrderById(id: number): Observable<ApiResponse<BorrowOrder>> {
    return this.http.get<ApiResponse<BorrowOrder>>(RestApiUrlConfig.REST_API_BORROW_ORDER_GET_BY_ID + id);
  }
}
