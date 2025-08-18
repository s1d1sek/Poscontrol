import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';
import { OrderRequest } from '../../models/order.model';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './order-form.html',
  styleUrl: './order-form.css'
})
export class OrderFormComponent implements OnInit {
  orderForm: FormGroup;
  products: Product[] = [];
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private orderService: OrderService,
    private productService: ProductService
  ) {
    this.orderForm = this.fb.group({
      customerName: ['', Validators.required],
      customerEmail: ['', [Validators.email]],
      orderItems: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadProducts();
    this.addOrderItem();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error loading products:', err)
    });
  }

  get orderItems(): FormArray {
    return this.orderForm.get('orderItems') as FormArray;
  }

  addOrderItem(): void {
    const orderItem = this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]]
    });
    this.orderItems.push(orderItem);
  }

  removeOrderItem(index: number): void {
    this.orderItems.removeAt(index);
  }

  calculateTotal(): number {
    let total = 0;
    this.orderItems.controls.forEach(control => {
      const productId = control.get('productId')?.value;
      const quantity = control.get('quantity')?.value;
      const product = this.products.find(p => p.id?.toString() === productId);
      if (product && quantity) {
        total += product.price * quantity;
      }
    });
    return total;
  }

  onSubmit(): void {
    if (this.orderForm.valid) {
      const orderRequest: OrderRequest = {
        customerName: this.orderForm.value.customerName,
        customerEmail: this.orderForm.value.customerEmail,
        orderItems: this.orderForm.value.orderItems.map((item: any) => ({
          productId: parseInt(item.productId),
          quantity: item.quantity
        }))
      };

      this.orderService.createOrder(orderRequest).subscribe({
        next: (response) => {
          this.successMessage = 'Order created successfully!';
          this.errorMessage = '';
          this.orderForm.reset();
          this.orderItems.clear();
          this.addOrderItem();
          this.loadProducts();
          setTimeout(() => this.successMessage = '', 5000);
        },
        error: (err) => {
          this.errorMessage = err.error?.error || 'Error creating order';
          this.successMessage = '';
        }
      });
    }
  }
}
