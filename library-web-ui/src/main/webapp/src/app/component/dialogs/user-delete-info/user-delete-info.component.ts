import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-user-delete-info',
  templateUrl: './user-delete-info.component.html',
  styleUrls: ['./user-delete-info.component.scss']
})
export class UserDeleteInfoComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
  }

}
