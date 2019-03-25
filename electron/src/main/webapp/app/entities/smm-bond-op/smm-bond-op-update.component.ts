import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';
import { SmmBondOpService } from './smm-bond-op.service';

@Component({
    selector: 'jhi-smm-bond-op-update',
    templateUrl: './smm-bond-op-update.component.html'
})
export class SmmBondOpUpdateComponent implements OnInit {
    smmBondOp: ISmmBondOp;
    isSaving: boolean;
    dt: string;
    writeRequestDt: string;

    constructor(protected smmBondOpService: SmmBondOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmBondOp }) => {
            this.smmBondOp = smmBondOp;
            this.dt = this.smmBondOp.dt != null ? this.smmBondOp.dt.format(DATE_TIME_FORMAT) : null;
            this.writeRequestDt = this.smmBondOp.writeRequestDt != null ? this.smmBondOp.writeRequestDt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smmBondOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        this.smmBondOp.writeRequestDt = this.writeRequestDt != null ? moment(this.writeRequestDt, DATE_TIME_FORMAT) : null;
        if (this.smmBondOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smmBondOpService.update(this.smmBondOp));
        } else {
            this.subscribeToSaveResponse(this.smmBondOpService.create(this.smmBondOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmBondOp>>) {
        result.subscribe((res: HttpResponse<ISmmBondOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
