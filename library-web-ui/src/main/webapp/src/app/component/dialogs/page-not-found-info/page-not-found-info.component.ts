import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-page-not-found-info',
  templateUrl: './page-not-found-info.component.html',
  styleUrls: ['./page-not-found-info.component.scss']
})
export class PageNotFoundInfoComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}
