import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-book-add-info',
  templateUrl: './book-add-info.component.html',
  styleUrls: ['./book-add-info.component.scss']
})
export class BookAddInfoComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}
