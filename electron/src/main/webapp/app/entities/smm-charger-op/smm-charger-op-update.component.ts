import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';
import { SmmChargerOpService } from './smm-charger-op.service';

@Component({
    selector: 'jhi-smm-charger-op-update',
    templateUrl: './smm-charger-op-update.component.html'
})
export class SmmChargerOpUpdateComponent implements OnInit {
    smmChargerOp: ISmmChargerOp;
    isSaving: boolean;
    dt: string;

    constructor(protected smmChargerOpService: SmmChargerOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmChargerOp }) => {
            this.smmChargerOp = smmChargerOp;
            this.dt = this.smmChargerOp.dt != null ? this.smmChargerOp.dt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.smmChargerOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        if (this.smmChargerOp.id !== undefined) {
            this.subscribeToSaveResponse(this.smmChargerOpService.update(this.smmChargerOp));
        } else {
            this.subscribeToSaveResponse(this.smmChargerOpService.create(this.smmChargerOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmChargerOp>>) {
        result.subscribe((res: HttpResponse<ISmmChargerOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
