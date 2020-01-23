import { User } from './user';
import { Book } from './book';

export class BorrowOrder {
  id: number;
  orderNumber: string;
  user: User;
  book: Book;
  creationDate: string;
}
