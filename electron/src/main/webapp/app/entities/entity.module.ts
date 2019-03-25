import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'electron-op',
                loadChildren: './electron-op/electron-op.module#ElectronElectronOpModule'
            },
            {
                path: 'particle-op',
                loadChildren: './particle-op/particle-op.module#ElectronParticleOpModule'
            },
            {
                path: 'smm-charger-op',
                loadChildren: './smm-charger-op/smm-charger-op.module#ElectronSmmChargerOpModule'
            },
            {
                path: 'smm-device-op',
                loadChildren: './smm-device-op/smm-device-op.module#ElectronSmmDeviceOpModule'
            },
            {
                path: 'smm-bond-op',
                loadChildren: './smm-bond-op/smm-bond-op.module#ElectronSmmBondOpModule'
            },
            {
                path: 'sms-charger-op',
                loadChildren: './sms-charger-op/sms-charger-op.module#ElectronSmsChargerOpModule'
            },
            {
                path: 'sms-device-op',
                loadChildren: './sms-device-op/sms-device-op.module#ElectronSmsDeviceOpModule'
            },
            {
                path: 'sms-bond-op',
                loadChildren: './sms-bond-op/sms-bond-op.module#ElectronSmsBondOpModule'
            },
            {
                path: 'electron-op',
                loadChildren: './electron-op/electron-op.module#ElectronElectronOpModule'
            },
            {
                path: 'particle-op',
                loadChildren: './particle-op/particle-op.module#ElectronParticleOpModule'
            },
            {
                path: 'smm-charger-op',
                loadChildren: './smm-charger-op/smm-charger-op.module#ElectronSmmChargerOpModule'
            },
            {
                path: 'smm-device-op',
                loadChildren: './smm-device-op/smm-device-op.module#ElectronSmmDeviceOpModule'
            },
            {
                path: 'smm-bond-op',
                loadChildren: './smm-bond-op/smm-bond-op.module#ElectronSmmBondOpModule'
            },
            {
                path: 'sms-charger-op',
                loadChildren: './sms-charger-op/sms-charger-op.module#ElectronSmsChargerOpModule'
            },
            {
                path: 'sms-device-op',
                loadChildren: './sms-device-op/sms-device-op.module#ElectronSmsDeviceOpModule'
            },
            {
                path: 'sms-bond-op',
                loadChildren: './sms-bond-op/sms-bond-op.module#ElectronSmsBondOpModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ElectronEntityModule {}
