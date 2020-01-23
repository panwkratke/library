import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { TokenInterceptor } from './service/auth/token.interceptor';
import { BookDetailsComponent } from './component/book-details/book-details.component';
import { BookSaveComponent } from './component/book-save/book-save.component';
import { BookUpdateComponent } from './component/book-update/book-update.component';
import { BooksViewComponent } from './component/books-view/books-view.component';
import { OrderDetailsComponent } from './component/order-details/order-details.component';
import { OrdersViewComponent } from './component/orders-view/orders-view.component';
import { PageNotFoundComponent } from './component/page-not-found/page-not-found.component';
import { UserDetailsComponent } from './component/user-details/user-details.component';
import { UserLoginComponent } from './component/user-login/user-login.component';
import { UserRegisterComponent } from './component/user-register/user-register.component';
import { UserUpdateComponent } from './component/user-update/user-update.component';
import { UserPanelComponent } from './component/user-panel/user-panel.component';
import { UsersViewComponent } from './component/users-view/users-view.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatDialogModule } from '@angular/material/dialog';
import { RegisterInfoComponent } from './component/dialogs/register-info/register-info.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ErrorInfoComponent } from './component/dialogs/error-info/error-info.component';
import { BorrowInfoComponent } from './component/dialogs/borrow-info/borrow-info.component';
import { ReturnInfoComponent } from './component/dialogs/return-info/return-info.component';
import { BookDeleteInfoComponent } from './component/dialogs/book-delete-info/book-delete-info.component';
import { UserDeleteInfoComponent } from './component/dialogs/user-delete-info/user-delete-info.component';
import { BookAddInfoComponent } from './component/dialogs/book-add-info/book-add-info.component';
import { UserGuard } from './service/auth/user.guard';
import { AdminGuard } from './service/auth/admin.guard';
import { UpdateInfoComponent } from './component/dialogs/update-info/update-info.component';
import { PageNotFoundInfoComponent } from './component/dialogs/page-not-found-info/page-not-found-info.component';

@NgModule({
  declarations: [
    AppComponent,
    BookDetailsComponent,
    BookSaveComponent,
    BookUpdateComponent,
    BooksViewComponent,
    OrderDetailsComponent,
    OrdersViewComponent,
    PageNotFoundComponent,
    UserDetailsComponent,
    UserLoginComponent,
    UserRegisterComponent,
    UserUpdateComponent,
    UserPanelComponent,
    UsersViewComponent,
    RegisterInfoComponent,
    ErrorInfoComponent,
    BorrowInfoComponent,
    ReturnInfoComponent,
    BookDeleteInfoComponent,
    UserDeleteInfoComponent,
    BookAddInfoComponent,
    UpdateInfoComponent,
    PageNotFoundInfoComponent
  ],
  entryComponents: [RegisterInfoComponent, ErrorInfoComponent, BorrowInfoComponent, ReturnInfoComponent,
                    UserDeleteInfoComponent, BookDeleteInfoComponent, BookAddInfoComponent, UpdateInfoComponent,
                    PageNotFoundInfoComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatExpansionModule,
    MatListModule,
    MatDividerModule,
    MatSidenavModule,
    ScrollingModule,
    MatDialogModule,
    MatTooltipModule
  ],

  providers: [ UserGuard, AdminGuard,
    { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
