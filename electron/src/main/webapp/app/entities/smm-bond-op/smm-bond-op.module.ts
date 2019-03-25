import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmmBondOpComponent,
    SmmBondOpDetailComponent,
    SmmBondOpUpdateComponent,
    SmmBondOpDeletePopupComponent,
    SmmBondOpDeleteDialogComponent,
    smmBondOpRoute,
    smmBondOpPopupRoute
} from './';

const ENTITY_STATES = [...smmBondOpRoute, ...smmBondOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmBondOpComponent,
        SmmBondOpDetailComponent,
        SmmBondOpUpdateComponent,
        SmmBondOpDeleteDialogComponent,
        SmmBondOpDeletePopupComponent
    ],
    entryComponents: [SmmBondOpComponent, SmmBondOpUpdateComponent, SmmBondOpDeleteDialogComponent, SmmBondOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmBondOpModule {}
