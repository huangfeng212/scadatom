import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ElectronSharedModule } from 'app/shared';
import {
    SmsBondOpComponent,
    SmsBondOpDetailComponent,
    SmsBondOpUpdateComponent,
    SmsBondOpDeletePopupComponent,
    SmsBondOpDeleteDialogComponent,
    smsBondOpRoute,
    smsBondOpPopupRoute
} from './';

const ENTITY_STATES = [...smsBondOpRoute, ...smsBondOpPopupRoute];

@NgModule({
    imports: [ElectronSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsBondOpComponent,
        SmsBondOpDetailComponent,
        SmsBondOpUpdateComponent,
        SmsBondOpDeleteDialogComponent,
        SmsBondOpDeletePopupComponent
    ],
    entryComponents: [SmsBondOpComponent, SmsBondOpUpdateComponent, SmsBondOpDeleteDialogComponent, SmsBondOpDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronSmsBondOpModule {}
