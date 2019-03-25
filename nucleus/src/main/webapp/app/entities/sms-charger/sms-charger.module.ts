import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmsChargerComponent,
    SmsChargerDetailComponent,
    SmsChargerUpdateComponent,
    SmsChargerDeletePopupComponent,
    SmsChargerDeleteDialogComponent,
    smsChargerRoute,
    smsChargerPopupRoute
} from './';

const ENTITY_STATES = [...smsChargerRoute, ...smsChargerPopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsChargerComponent,
        SmsChargerDetailComponent,
        SmsChargerUpdateComponent,
        SmsChargerDeleteDialogComponent,
        SmsChargerDeletePopupComponent
    ],
    entryComponents: [SmsChargerComponent, SmsChargerUpdateComponent, SmsChargerDeleteDialogComponent, SmsChargerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmsChargerModule {}
