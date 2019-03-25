import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticleOp } from 'app/shared/model/particle-op.model';

@Component({
    selector: 'jhi-particle-op-detail',
    templateUrl: './particle-op-detail.component.html'
})
export class ParticleOpDetailComponent implements OnInit {
    particleOp: IParticleOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ particleOp }) => {
            this.particleOp = particleOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
