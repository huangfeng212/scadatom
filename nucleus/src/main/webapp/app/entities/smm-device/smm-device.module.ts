import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmmDeviceComponent,
    SmmDeviceDetailComponent,
    SmmDeviceUpdateComponent,
    SmmDeviceDeletePopupComponent,
    SmmDeviceDeleteDialogComponent,
    smmDeviceRoute,
    smmDevicePopupRoute
} from './';

const ENTITY_STATES = [...smmDeviceRoute, ...smmDevicePopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmDeviceComponent,
        SmmDeviceDetailComponent,
        SmmDeviceUpdateComponent,
        SmmDeviceDeleteDialogComponent,
        SmmDeviceDeletePopupComponent
    ],
    entryComponents: [SmmDeviceComponent, SmmDeviceUpdateComponent, SmmDeviceDeleteDialogComponent, SmmDeviceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmmDeviceModule {}
