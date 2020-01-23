import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-book-delete-info',
  templateUrl: './book-delete-info.component.html',
  styleUrls: ['./book-delete-info.component.scss']
})
export class BookDeleteInfoComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}
