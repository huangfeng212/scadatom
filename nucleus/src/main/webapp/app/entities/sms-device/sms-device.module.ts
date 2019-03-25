import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmsDeviceComponent,
    SmsDeviceDetailComponent,
    SmsDeviceUpdateComponent,
    SmsDeviceDeletePopupComponent,
    SmsDeviceDeleteDialogComponent,
    smsDeviceRoute,
    smsDevicePopupRoute
} from './';

const ENTITY_STATES = [...smsDeviceRoute, ...smsDevicePopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsDeviceComponent,
        SmsDeviceDetailComponent,
        SmsDeviceUpdateComponent,
        SmsDeviceDeleteDialogComponent,
        SmsDeviceDeletePopupComponent
    ],
    entryComponents: [SmsDeviceComponent, SmsDeviceUpdateComponent, SmsDeviceDeleteDialogComponent, SmsDeviceDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmsDeviceModule {}
