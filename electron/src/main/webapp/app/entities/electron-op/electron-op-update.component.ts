import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IElectronOp } from 'app/shared/model/electron-op.model';
import { ElectronOpService } from './electron-op.service';

@Component({
    selector: 'jhi-electron-op-update',
    templateUrl: './electron-op-update.component.html'
})
export class ElectronOpUpdateComponent implements OnInit {
    electronOp: IElectronOp;
    isSaving: boolean;
    dt: string;

    constructor(protected electronOpService: ElectronOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ electronOp }) => {
            this.electronOp = electronOp;
            this.dt = this.electronOp.dt != null ? this.electronOp.dt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.electronOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        if (this.electronOp.id !== undefined) {
            this.subscribeToSaveResponse(this.electronOpService.update(this.electronOp));
        } else {
            this.subscribeToSaveResponse(this.electronOpService.create(this.electronOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IElectronOp>>) {
        result.subscribe((res: HttpResponse<IElectronOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
