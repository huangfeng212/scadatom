import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmsDeviceOpComponent,
    SmsDeviceOpDetailComponent,
    SmsDeviceOpUpdateComponent,
    SmsDeviceOpDeletePopupComponent,
    SmsDeviceOpDeleteDialogComponent,
    smsDeviceOpRoute,
    smsDeviceOpPopupRoute
} from './';

const ENTITY_STATES = [...smsDeviceOpRoute, ...smsDeviceOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsDeviceOpComponent,
        SmsDeviceOpDetailComponent,
        SmsDeviceOpUpdateComponent,
        SmsDeviceOpDeleteDialogComponent,
        SmsDeviceOpDeletePopupComponent
    ],
    entryComponents: [SmsDeviceOpComponent, SmsDeviceOpUpdateComponent, SmsDeviceOpDeleteDialogComponent, SmsDeviceOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsDeviceOpModule {}
