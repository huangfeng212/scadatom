import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmsDeviceOpComponent, SmsDeviceOpDetailComponent, smsDeviceOpRoute } from './';

const ENTITY_STATES = [...smsDeviceOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmsDeviceOpComponent, SmsDeviceOpDetailComponent],
    entryComponents: [SmsDeviceOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsDeviceOpModule {}
