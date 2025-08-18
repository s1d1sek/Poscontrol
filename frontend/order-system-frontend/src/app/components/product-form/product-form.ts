import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-form.html',
  styleUrl: './product-form.css'
})
export class ProductFormComponent {
  product: Product = {
    name: '',
    description: '',
    price: 0,
    stockQuantity: 0,
    minStockLevel: 0
  };
  
  successMessage = '';
  errorMessage = '';

  constructor(
    private productService: ProductService,
    private router: Router
  ) {}

  onSubmit(): void {
    if (!this.product.name || this.product.price <= 0) {
      this.errorMessage = 'Please fill in all required fields correctly';
      return;
    }

    this.productService.createProduct(this.product).subscribe({
      next: (response: any) => {
        this.successMessage = 'Product added successfully!';
        this.errorMessage = '';
        this.product = {
          name: '',
          description: '',
          price: 0,
          stockQuantity: 0,
          minStockLevel: 0
        };
        setTimeout(() => {
          this.router.navigate(['/products']);
        }, 2000);
      },
      error: (error: any) => {
        this.errorMessage = 'Error adding product';
        this.successMessage = '';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/products']);
  }
}
