import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserLoginComponent } from './component/user-login/user-login.component';
import { UserRegisterComponent } from './component/user-register/user-register.component';
import { UserPanelComponent } from './component/user-panel/user-panel.component';
import { PageNotFoundComponent } from './component/page-not-found/page-not-found.component';
import { OrdersViewComponent } from './component/orders-view/orders-view.component';
import { BooksViewComponent } from './component/books-view/books-view.component';
import { UserUpdateComponent } from './component/user-update/user-update.component';
import { BookDetailsComponent } from './component/book-details/book-details.component';
import { OrderDetailsComponent } from './component/order-details/order-details.component';
import { UsersViewComponent } from './component/users-view/users-view.component';
import { UserDetailsComponent } from './component/user-details/user-details.component';
import { BookSaveComponent } from './component/book-save/book-save.component';
import { BookUpdateComponent } from './component/book-update/book-update.component';
import { UserGuard } from './service/auth/user.guard';
import { AdminGuard } from './service/auth/admin.guard';


const routes: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full'
  },
  {
    path: 'users/login',
    component: UserLoginComponent
  },
  {
    path: 'users/register',
    component: UserRegisterComponent
  },
  {
    path: 'library/userpanel',
    component: UserPanelComponent,
    canActivate: [UserGuard],
    children: [
      {
        path: 'ordersview',
        component: OrdersViewComponent,
        children: [
          {
            path: ':id',
            component: OrderDetailsComponent
          },
        ]
      },
      {
        path: 'booksview',
        component: BooksViewComponent,
        children: [
          {
            path: ':id',
            component: BookDetailsComponent
          },
        ]
      },
      {
        path: 'usersview',
        component: UsersViewComponent,
        canActivate: [AdminGuard],
        children: [
          {
            path: ':id',
            component: UserDetailsComponent
          },
        ]
      },
      {
        path: 'booksview/addBook/new',
        component: BookSaveComponent,
        canActivate: [AdminGuard],
      },
      {
        path: 'booksview/updateBook/upd',
        component: BookUpdateComponent,
        canActivate: [AdminGuard],
      },
      {
        path: ':id',
        component: UserUpdateComponent
      },
    ]
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
