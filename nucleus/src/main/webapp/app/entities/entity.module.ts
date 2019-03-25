import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'electron',
                loadChildren: './electron/electron.module#NucleusElectronModule'
            },
            {
                path: 'particle',
                loadChildren: './particle/particle.module#NucleusParticleModule'
            },
            {
                path: 'smm-charger',
                loadChildren: './smm-charger/smm-charger.module#NucleusSmmChargerModule'
            },
            {
                path: 'smm-device',
                loadChildren: './smm-device/smm-device.module#NucleusSmmDeviceModule'
            },
            {
                path: 'smm-bond',
                loadChildren: './smm-bond/smm-bond.module#NucleusSmmBondModule'
            },
            {
                path: 'sms-charger',
                loadChildren: './sms-charger/sms-charger.module#NucleusSmsChargerModule'
            },
            {
                path: 'sms-device',
                loadChildren: './sms-device/sms-device.module#NucleusSmsDeviceModule'
            },
            {
                path: 'sms-bond',
                loadChildren: './sms-bond/sms-bond.module#NucleusSmsBondModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NucleusEntityModule {}
