import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IElectronOp } from 'app/shared/model/electron-op.model';
import { ElectronOpService } from 'app/entities/electron-op/electron-op.service';
import { OpCtrlReq } from 'app/shared/model/operation.model';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-electron-op-detail',
    templateUrl: './electron-op-detail.component.html'
})
export class ElectronOpDetailComponent implements OnInit {
    electronOp: IElectronOp;

    constructor(protected activatedRoute: ActivatedRoute, protected electronOpService: ElectronOpService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ electronOp }) => {
            this.electronOp = electronOp;
        });
    }

    previousState() {
        window.history.back();
    }

    writeCommand(value: string) {
        this.subscribeToSaveResponse(this.electronOpService.ctrl(new OpCtrlReq(this.electronOp.id, value)));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.previousState();
    }

    protected onSaveError() {}
}
