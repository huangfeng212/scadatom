import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmmDeviceOpComponent, SmmDeviceOpDetailComponent, smmDeviceOpRoute } from './';

const ENTITY_STATES = [...smmDeviceOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmmDeviceOpComponent, SmmDeviceOpDetailComponent],
    entryComponents: [SmmDeviceOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmDeviceOpModule {}
