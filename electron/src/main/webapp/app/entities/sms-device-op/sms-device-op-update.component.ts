import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';
import { SmsDeviceOpService } from './sms-device-op.service';

@Component({
    selector: 'jhi-sms-device-op-update',
    templateUrl: './sms-device-op-update.component.html'
})
export class SmsDeviceOpUpdateComponent implements OnInit {
    smsDeviceOp: ISmsDeviceOp;
    isSaving: boolean;
    dt: string;

    constructor(protected smsDeviceOpService: SmsDeviceOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsDeviceOp }) => {
            this.smsDeviceOp = smsDeviceOp;
            this.dt = this.smsDeviceOp.dt != null ? this.smsDeviceOp.dt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smsDeviceOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        if (this.smsDeviceOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smsDeviceOpService.update(this.smsDeviceOp));
        } else {
            this.subscribeToSaveResponse(this.smsDeviceOpService.create(this.smsDeviceOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsDeviceOp>>) {
        result.subscribe((res: HttpResponse<ISmsDeviceOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
