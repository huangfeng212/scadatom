import { NgModule } from '@angular/core';

import { ElectronSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ElectronSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ElectronSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ElectronSharedCommonModule {}
