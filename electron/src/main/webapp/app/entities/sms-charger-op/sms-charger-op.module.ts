import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmsChargerOpComponent,
    SmsChargerOpDetailComponent,
    SmsChargerOpUpdateComponent,
    SmsChargerOpDeletePopupComponent,
    SmsChargerOpDeleteDialogComponent,
    smsChargerOpRoute,
    smsChargerOpPopupRoute
} from './';

const ENTITY_STATES = [...smsChargerOpRoute, ...smsChargerOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsChargerOpComponent,
        SmsChargerOpDetailComponent,
        SmsChargerOpUpdateComponent,
        SmsChargerOpDeleteDialogComponent,
        SmsChargerOpDeletePopupComponent
    ],
    entryComponents: [
        SmsChargerOpComponent,
        SmsChargerOpUpdateComponent,
        SmsChargerOpDeleteDialogComponent,
        SmsChargerOpDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsChargerOpModule {}
