import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { Order, OrderStatus } from '../../models/order.model';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-list.html',
  styleUrl: './order-list.css'
})
export class OrderListComponent implements OnInit {
  orders: Order[] = [];
  loading = false;
  error = '';

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.loading = true;
    this.orderService.getOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load orders';
        this.loading = false;
        console.error('Error loading orders:', err);
      }
    });
  }

  getStatusColor(status: OrderStatus): string {
    switch(status) {
      case OrderStatus.PENDING: return 'warning';
      case OrderStatus.COMPLETED: return 'success';
      case OrderStatus.CANCELLED: return 'danger';
      default: return 'secondary';
    }
  }

  cancelOrder(orderId: number): void {
    if (confirm('Are you sure you want to cancel this order?')) {
      this.orderService.cancelOrder(orderId).subscribe({
        next: () => {
          this.loadOrders();
        },
        error: (err) => console.error('Error cancelling order:', err)
      });
    }
  }

  completeOrder(orderId: number): void {
    this.orderService.completeOrder(orderId).subscribe({
      next: () => {
        this.loadOrders();
      },
      error: (err) => console.error('Error completing order:', err)
    });
  }
}
