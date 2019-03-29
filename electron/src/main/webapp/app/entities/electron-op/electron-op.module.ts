import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { ElectronOpComponent, ElectronOpDetailComponent, electronOpRoute } from './';

const ENTITY_STATES = [...electronOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ElectronOpComponent, ElectronOpDetailComponent],
    entryComponents: [ElectronOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronElectronOpModule {}
