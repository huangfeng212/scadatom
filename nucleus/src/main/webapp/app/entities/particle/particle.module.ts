import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    ParticleComponent,
    ParticleDetailComponent,
    ParticleUpdateComponent,
    ParticleDeletePopupComponent,
    ParticleDeleteDialogComponent,
    particleRoute,
    particlePopupRoute
} from './';

const ENTITY_STATES = [...particleRoute, ...particlePopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ParticleComponent,
        ParticleDetailComponent,
        ParticleUpdateComponent,
        ParticleDeleteDialogComponent,
        ParticleDeletePopupComponent
    ],
    entryComponents: [ParticleComponent, ParticleUpdateComponent, ParticleDeleteDialogComponent, ParticleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusParticleModule {}
