import { Book } from './book';

export class User {
  id?: number;
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  token: string;
  role?: string;
  borrowedBooks?: Array<Book>;
  creationDate: string;
}
