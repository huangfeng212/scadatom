import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmmDeviceOpComponent,
    SmmDeviceOpDetailComponent,
    SmmDeviceOpUpdateComponent,
    SmmDeviceOpDeletePopupComponent,
    SmmDeviceOpDeleteDialogComponent,
    smmDeviceOpRoute,
    smmDeviceOpPopupRoute
} from './';

const ENTITY_STATES = [...smmDeviceOpRoute, ...smmDeviceOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmDeviceOpComponent,
        SmmDeviceOpDetailComponent,
        SmmDeviceOpUpdateComponent,
        SmmDeviceOpDeleteDialogComponent,
        SmmDeviceOpDeletePopupComponent
    ],
    entryComponents: [SmmDeviceOpComponent, SmmDeviceOpUpdateComponent, SmmDeviceOpDeleteDialogComponent, SmmDeviceOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmDeviceOpModule {}
