import { NgModule } from '@angular/core';

import { NucleusSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [NucleusSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [NucleusSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class NucleusSharedCommonModule {}
