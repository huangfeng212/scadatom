import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IElectron} from 'app/shared/model/electron.model';
import {IElectronOp} from 'app/shared/model/electron-op.model';
import {ElectronService} from 'app/entities/electron/electron.service';
import {OpCtrlReq} from 'app/shared/model/operation.model';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'jhi-electron-detail',
  templateUrl: './electron-detail.component.html'
})
export class ElectronDetailComponent implements OnInit {
  electron: IElectron;
  electronOp: IElectronOp;

  constructor(protected activatedRoute: ActivatedRoute, protected electronService: ElectronService) {
  }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({electron}) => {
      this.electron = electron;
      this.electronService.view(this.electron.id).subscribe(value => this.electronOp = value.body);
    });
  }

  previousState() {
    window.history.back();
  }

  writeCommand(value: string) {
    this.subscribeToSaveResponse(this.electronService.ctrl(new OpCtrlReq(this.electronOp.id, value)));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
    result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.previousState();
  }

  protected onSaveError() {
  }
}
