import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    ElectronOpComponent,
    ElectronOpDetailComponent,
    ElectronOpUpdateComponent,
    ElectronOpDeletePopupComponent,
    ElectronOpDeleteDialogComponent,
    electronOpRoute,
    electronOpPopupRoute
} from './';

const ENTITY_STATES = [...electronOpRoute, ...electronOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ElectronOpComponent,
        ElectronOpDetailComponent,
        ElectronOpUpdateComponent,
        ElectronOpDeleteDialogComponent,
        ElectronOpDeletePopupComponent
    ],
    entryComponents: [ElectronOpComponent, ElectronOpUpdateComponent, ElectronOpDeleteDialogComponent, ElectronOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronElectronOpModule {}
