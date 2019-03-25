import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    ParticleOpComponent,
    ParticleOpDetailComponent,
    ParticleOpUpdateComponent,
    ParticleOpDeletePopupComponent,
    ParticleOpDeleteDialogComponent,
    particleOpRoute,
    particleOpPopupRoute
} from './';

const ENTITY_STATES = [...particleOpRoute, ...particleOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ParticleOpComponent,
        ParticleOpDetailComponent,
        ParticleOpUpdateComponent,
        ParticleOpDeleteDialogComponent,
        ParticleOpDeletePopupComponent
    ],
    entryComponents: [ParticleOpComponent, ParticleOpUpdateComponent, ParticleOpDeleteDialogComponent, ParticleOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronParticleOpModule {}
