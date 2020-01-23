import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-borrow-info',
  templateUrl: './borrow-info.component.html',
  styleUrls: ['./borrow-info.component.scss']
})
export class BorrowInfoComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}
