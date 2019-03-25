import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IParticleOp } from 'app/shared/model/particle-op.model';
import { ParticleOpService } from './particle-op.service';

@Component({
    selector: 'jhi-particle-op-update',
    templateUrl: './particle-op-update.component.html'
})
export class ParticleOpUpdateComponent implements OnInit {
    particleOp: IParticleOp;
    isSaving: boolean;
    dt: string;
    writtenDt: string;

    constructor(protected particleOpService: ParticleOpService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ particleOp }) => {
            this.particleOp = particleOp;
            this.dt = this.particleOp.dt != null ? this.particleOp.dt.format(DATE_TIME_FORMAT) : null;
            this.writtenDt = this.particleOp.writtenDt != null ? this.particleOp.writtenDt.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.particleOp.dt = this.dt != null ? moment(this.dt, DATE_TIME_FORMAT) : null;
        this.particleOp.writtenDt = this.writtenDt != null ? moment(this.writtenDt, DATE_TIME_FORMAT) : null;
        if (this.particleOp.id !== undefined) {
            this.subscribeToSaveResponse(this.particleOpService.update(this.particleOp));
        } else {
            this.subscribeToSaveResponse(this.particleOpService.create(this.particleOp));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticleOp>>) {
        result.subscribe((res: HttpResponse<IParticleOp>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
