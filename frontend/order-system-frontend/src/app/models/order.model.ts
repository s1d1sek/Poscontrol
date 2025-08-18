import { Product } from './product.model';

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export interface Order {
  id?: number;
  orderDate: Date;
  customerName: string;
  customerEmail?: string;
  totalAmount: number;
  status: OrderStatus;
  orderItems: OrderItem[];
}

export interface OrderItem {
  id?: number;
  product: Product;
  quantity: number;
  unitPrice: number;
}

export interface OrderRequest {
  customerName: string;
  customerEmail?: string;
  orderItems: OrderItemRequest[];
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}
