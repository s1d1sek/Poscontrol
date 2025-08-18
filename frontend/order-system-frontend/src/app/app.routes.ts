import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list';
import { ProductFormComponent } from './components/product-form/product-form';
import { OrderFormComponent } from './components/order-form/order-form';
import { OrderListComponent } from './components/order-list/order-list';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'products/new', component: ProductFormComponent },
  { path: 'new-order', component: OrderFormComponent },
  { path: 'orders', component: OrderListComponent }
];
