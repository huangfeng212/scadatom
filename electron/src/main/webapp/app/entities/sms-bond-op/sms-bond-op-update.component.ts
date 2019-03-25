import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';
import { SmsBondOpService } from './sms-bond-op.service';

@Component({
    selector: 'jhi-sms-bond-op-update',
    templateUrl: './sms-bond-op-update.component.html'
})
export class SmsBondOpUpdateComponent implements OnInit {
    smsBondOp: ISmsBondOp;
    isSaving: boolean;
    dt: string;
    writtenDt: string;

    constructor(protected smsBondOpService: SmsBondOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsBondOp }) => {
            this.smsBondOp = smsBondOp;
            this.dt = this.smsBondOp.dt != null ? this.smsBondOp.dt.format(DATE_TIME_FORMAT) : null;
            this.writtenDt = this.smsBondOp.writtenDt != null ? this.smsBondOp.writtenDt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smsBondOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        this.smsBondOp.writtenDt = this.writtenDt != null ? moment(this.writtenDt, DATE_TIME_FORMAT) : null;
        if (this.smsBondOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smsBondOpService.update(this.smsBondOp));
        } else {
            this.subscribeToSaveResponse(this.smsBondOpService.create(this.smsBondOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsBondOp>>) {
        result.subscribe((res: HttpResponse<ISmsBondOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
