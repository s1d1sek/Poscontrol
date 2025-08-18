import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, OrderRequest, OrderStatus } from '../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/orders';
  
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  createOrder(orderRequest: OrderRequest): Observable<Order> {
    return this.http.post<Order>(this.apiUrl, orderRequest, this.httpOptions);
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  getOrder(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${id}`);
  }

  getOrdersByStatus(status: OrderStatus): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/status/${status}`);
  }

  getTodaysOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/today`);
  }

  updateOrderStatus(id: number, status: OrderStatus): Observable<Order> {
    const url = `${this.apiUrl}/${id}/status`;
    return this.http.put<Order>(url, { status }, this.httpOptions);
  }

  cancelOrder(id: number): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${id}/cancel`, {}, this.httpOptions);
  }

  completeOrder(id: number): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${id}/complete`, {}, this.httpOptions);
  }

  getOrderStatistics(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/statistics`);
  }
}
