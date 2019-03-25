import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmmChargerComponent,
    SmmChargerDetailComponent,
    SmmChargerUpdateComponent,
    SmmChargerDeletePopupComponent,
    SmmChargerDeleteDialogComponent,
    smmChargerRoute,
    smmChargerPopupRoute
} from './';

const ENTITY_STATES = [...smmChargerRoute, ...smmChargerPopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmChargerComponent,
        SmmChargerDetailComponent,
        SmmChargerUpdateComponent,
        SmmChargerDeleteDialogComponent,
        SmmChargerDeletePopupComponent
    ],
    entryComponents: [SmmChargerComponent, SmmChargerUpdateComponent, SmmChargerDeleteDialogComponent, SmmChargerDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmmChargerModule {}
