import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmmBondComponent,
    SmmBondDeleteDialogComponent,
    SmmBondDeletePopupComponent,
    SmmBondDetailComponent,
    smmBondPopupRoute,
    smmBondRoute,
    SmmBondUpdateComponent
} from './';

const ENTITY_STATES = [...smmBondRoute, ...smmBondPopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmBondComponent,
        SmmBondDetailComponent,
        SmmBondUpdateComponent,
        SmmBondDeleteDialogComponent,
        SmmBondDeletePopupComponent
    ],
    entryComponents: [SmmBondComponent, SmmBondUpdateComponent, SmmBondDeleteDialogComponent, SmmBondDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmmBondModule {}
