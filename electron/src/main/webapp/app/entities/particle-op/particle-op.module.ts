import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { ParticleOpComponent, ParticleOpDetailComponent, particleOpRoute } from './';

const ENTITY_STATES = [...particleOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ParticleOpComponent, ParticleOpDetailComponent],
    entryComponents: [ParticleOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronParticleOpModule {}
