import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmsChargerOpComponent, SmsChargerOpDetailComponent, smsChargerOpRoute } from './';

const ENTITY_STATES = [...smsChargerOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmsChargerOpComponent, SmsChargerOpDetailComponent],
    entryComponents: [SmsChargerOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsChargerOpModule {}
