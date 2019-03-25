import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmmDevice } from 'app/shared/model/smm-device.model';
import { SmmDeviceService } from './smm-device.service';
import { SmmDeviceComponent } from './smm-device.component';
import { SmmDeviceDetailComponent } from './smm-device-detail.component';
import { SmmDeviceUpdateComponent } from './smm-device-update.component';
import { SmmDeviceDeletePopupComponent } from './smm-device-delete-dialog.component';
import { ISmmDevice } from 'app/shared/model/smm-device.model';
import { ISmmCharger } from 'app/shared/model/smm-charger.model';

@Injectable({ providedIn: 'root' })
export class SmmDeviceResolve implements Resolve<ISmmDevice> {
    constructor(private service: SmmDeviceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmDevice> {
        const id = route.params['id'] ? route.params['id'] : null;
        const smmChargerId = route.params['smmChargerId'] ? route.params['smmChargerId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmDevice>) => response.ok),
                map((smmDevice: HttpResponse<SmmDevice>) => smmDevice.body)
            );
        } else if (smmChargerId) {
            return of({ smmCharger: { id: smmChargerId } } as ISmmCharger);
        }
        return of(new SmmDevice());
    }
}

export const smmDeviceRoute: Routes = [
    {
        path: '',
        component: SmmDeviceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmDeviceDetailComponent,
        resolve: {
            smmDevice: SmmDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:smmChargerId',
        component: SmmDeviceUpdateComponent,
        resolve: {
            smmDevice: SmmDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmmDeviceUpdateComponent,
        resolve: {
            smmDevice: SmmDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDevices'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smmDevicePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmmDeviceDeletePopupComponent,
        resolve: {
            smmDevice: SmmDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDevices'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
