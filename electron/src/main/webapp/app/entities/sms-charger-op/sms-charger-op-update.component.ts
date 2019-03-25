import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';
import { SmsChargerOpService } from './sms-charger-op.service';

@Component({
    selector: 'jhi-sms-charger-op-update',
    templateUrl: './sms-charger-op-update.component.html'
})
export class SmsChargerOpUpdateComponent implements OnInit {
    smsChargerOp: ISmsChargerOp;
    isSaving: boolean;
    dt: string;

    constructor(protected smsChargerOpService: SmsChargerOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsChargerOp }) => {
            this.smsChargerOp = smsChargerOp;
            this.dt = this.smsChargerOp.dt != null ? this.smsChargerOp.dt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smsChargerOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        if (this.smsChargerOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smsChargerOpService.update(this.smsChargerOp));
        } else {
            this.subscribeToSaveResponse(this.smsChargerOpService.create(this.smsChargerOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsChargerOp>>) {
        result.subscribe((res: HttpResponse<ISmsChargerOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
