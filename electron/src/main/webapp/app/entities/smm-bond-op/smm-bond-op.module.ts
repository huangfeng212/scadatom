import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import { SmmBondOpComponent, SmmBondOpDetailComponent, smmBondOpRoute } from './';

const ENTITY_STATES = [...smmBondOpRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SmmBondOpComponent, SmmBondOpDetailComponent],
    entryComponents: [SmmBondOpComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmBondOpModule {}
