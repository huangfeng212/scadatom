import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';
import { SmmDeviceOpService } from './smm-device-op.service';

@Component({
    selector: 'jhi-smm-device-op-update',
    templateUrl: './smm-device-op-update.component.html'
})
export class SmmDeviceOpUpdateComponent implements OnInit {
    smmDeviceOp: ISmmDeviceOp;
    isSaving: boolean;
    dt: string;

    constructor(protected smmDeviceOpService: SmmDeviceOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmDeviceOp }) => {
            this.smmDeviceOp = smmDeviceOp;
            this.dt = this.smmDeviceOp.dt != null ? this.smmDeviceOp.dt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smmDeviceOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        if (this.smmDeviceOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smmDeviceOpService.update(this.smmDeviceOp));
        } else {
            this.subscribeToSaveResponse(this.smmDeviceOpService.create(this.smmDeviceOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmDeviceOp>>) {
        result.subscribe((res: HttpResponse<ISmmDeviceOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
