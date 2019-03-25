import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmmChargerOpComponent,
    SmmChargerOpDetailComponent,
    SmmChargerOpUpdateComponent,
    SmmChargerOpDeletePopupComponent,
    SmmChargerOpDeleteDialogComponent,
    smmChargerOpRoute,
    smmChargerOpPopupRoute
} from './';

const ENTITY_STATES = [...smmChargerOpRoute, ...smmChargerOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmmChargerOpComponent,
        SmmChargerOpDetailComponent,
        SmmChargerOpUpdateComponent,
        SmmChargerOpDeleteDialogComponent,
        SmmChargerOpDeletePopupComponent
    ],
    entryComponents: [
        SmmChargerOpComponent,
        SmmChargerOpUpdateComponent,
        SmmChargerOpDeleteDialogComponent,
        SmmChargerOpDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmmChargerOpModule {}
