import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Book } from '../book.model';
import { BookService } from '../book.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './book-form.component.html',
  styleUrl: './book-form.component.css'
})
export class BookFormComponent {
  @Output() bookAdded = new EventEmitter<void>();

  book: Book = {
    name: '',
    author: '',
    description: ''
  };

  constructor(private bookService: BookService) { }

  onSubmit(): void {
    this.bookService.addBook(this.book).subscribe(
      () => {
        this.bookAdded.emit();
        this.resetForm();
      },
      (error: any) => {
        console.error(error);
      }
    )
  }

  resetForm(): void {
    this.book = {
      name: '',
      author: '',
      description: ''
    };
  }

}
