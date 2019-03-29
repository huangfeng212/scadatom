import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmmChargerOpComponent, SmmChargerOpDetailComponent, smmChargerOpRoute } from './';

const ENTITY_STATES = [...smmChargerOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmmChargerOpComponent, SmmChargerOpDetailComponent],
    entryComponents: [SmmChargerOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmChargerOpModule {}
