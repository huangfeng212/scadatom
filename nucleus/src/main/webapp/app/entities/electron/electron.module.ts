import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    ElectronComponent,
    ElectronDetailComponent,
    ElectronUpdateComponent,
    ElectronDeletePopupComponent,
    ElectronDeleteDialogComponent,
    electronRoute,
    electronPopupRoute
} from './';

const ENTITY_STATES = [...electronRoute, ...electronPopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ElectronComponent,
        ElectronDetailComponent,
        ElectronUpdateComponent,
        ElectronDeleteDialogComponent,
        ElectronDeletePopupComponent
    ],
    entryComponents: [ElectronComponent, ElectronUpdateComponent, ElectronDeleteDialogComponent, ElectronDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusElectronModule {}
