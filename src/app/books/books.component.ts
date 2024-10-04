import { Component, OnInit } from '@angular/core';
import { Book } from '../book.model';
import { BookService } from '../book.service';
import { CommonModule, NgFor } from '@angular/common';
import { BookFormComponent } from '../book-form/book-form.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [NgFor, CommonModule, BookFormComponent, FormsModule],
  templateUrl: './books.component.html',
  styleUrl: './books.component.css'
})
export class BooksComponent implements OnInit {
  books: Book[] = [];
  editingBook: Book | null = null;
  addBook: Boolean = false;
  
  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.getBooks();
  }

  getBooks(): void {
    this.bookService.getBooks().subscribe(
      (response: Book[]) => {
        console.log(response);
        this.books = response;
      }, 
      (error: any)  => {
        console.error(error);
      }
    )
  }

  startEdit(book: Book): void {
    this.editingBook = { ...book };
  }

  cancelEdit(): void {
    this.editingBook = null;
  }

  saveEdit(): void {
    if (this.editingBook) {
      this.bookService.updateBook(this.editingBook).subscribe(
        (response: Book) => {
          console.log(response);
          this.getBooks();
          this.editingBook = null;
        },
        (error: any) => {
          console.error(error);
        }
      );
    }
  }

  deleteBook(id: number): void {
    this.bookService.deleteBook(id).subscribe(
      () => {
        this.getBooks();
      },
      (error: any) => {
        console.error(error);
      }
    )
  }

  startAddBook(): void {
    this.addBook = true;
  }

  onBookAdded(): void {
    this.getBooks();
    this.addBook = false;
  }
}
