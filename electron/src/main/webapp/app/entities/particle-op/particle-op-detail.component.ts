import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticleOp } from 'app/shared/model/particle-op.model';
import { ParticleOpService } from 'app/entities/particle-op/particle-op.service';
import { OpCtrlReq } from 'app/shared/model/operation.model';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-particle-op-detail',
    templateUrl: './particle-op-detail.component.html'
})
export class ParticleOpDetailComponent implements OnInit {
    particleOp: IParticleOp;

    constructor(protected activatedRoute: ActivatedRoute, protected particleOpService: ParticleOpService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ particleOp }) => {
            this.particleOp = particleOp;
        });
    }

    previousState() {
        window.history.back();
    }

    writeCommand(value: string) {
        this.subscribeToSaveResponse(this.particleOpService.ctrl(new OpCtrlReq(this.particleOp.id, value)));
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>) {
        result.subscribe((res: HttpResponse<any>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.previousState();
    }

    protected onSaveError() {}
}
