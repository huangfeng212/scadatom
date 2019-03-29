import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmsBondOpComponent, SmsBondOpDetailComponent, smsBondOpRoute } from './';

const ENTITY_STATES = [...smsBondOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmsBondOpComponent, SmsBondOpDetailComponent],
    entryComponents: [SmsBondOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsBondOpModule {}
