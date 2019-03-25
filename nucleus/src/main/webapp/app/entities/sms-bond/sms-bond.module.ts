import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NucleusSharedModule } from 'app/shared';
import {
    SmsBondComponent,
    SmsBondDetailComponent,
    SmsBondUpdateComponent,
    SmsBondDeletePopupComponent,
    SmsBondDeleteDialogComponent,
    smsBondRoute,
    smsBondPopupRoute
} from './';

const ENTITY_STATES = [...smsBondRoute, ...smsBondPopupRoute];

@NgModule({
    imports: [NucleusSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SmsBondComponent,
        SmsBondDetailComponent,
        SmsBondUpdateComponent,
        SmsBondDeleteDialogComponent,
        SmsBondDeletePopupComponent
    ],
    entryComponents: [SmsBondComponent, SmsBondUpdateComponent, SmsBondDeleteDialogComponent, SmsBondDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusSmsBondModule {}
