import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticle } from 'app/shared/model/particle.model';

@Component({
    selector: 'jhi-particle-detail',
    templateUrl: './particle-detail.component.html'
})
export class ParticleDetailComponent implements OnInit {
    particle: IParticle;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ particle }) => {
            this.particle = particle;
        });
    }

    previousState() {
        window.history.back();
    }
}
