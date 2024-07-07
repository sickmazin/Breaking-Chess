import { Component } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {MAN_AVATAR , WOMAN_AVATAR} from "../../support/constants";

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.scss'
})
export class AvatarComponent {
  public avatarSelected!: string;

  constructor(
      public dialogRef: MatDialogRef<AvatarComponent>,
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  protected readonly WOMAN_AVATAR = WOMAN_AVATAR;
  protected readonly MAN_AVATAR = MAN_AVATAR;
}
