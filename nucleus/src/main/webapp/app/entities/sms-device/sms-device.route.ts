import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISmsDevice, SmsDevice } from 'app/shared/model/sms-device.model';
import { SmsDeviceService } from './sms-device.service';
import { SmsDeviceComponent } from './sms-device.component';
import { SmsDeviceDetailComponent } from './sms-device-detail.component';
import { SmsDeviceUpdateComponent } from './sms-device-update.component';
import { SmsDeviceDeletePopupComponent } from './sms-device-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SmsDeviceResolve implements Resolve<ISmsDevice> {
    constructor(private service: SmsDeviceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsDevice> {
        const id = route.params['id'] ? route.params['id'] : null;
        const smsChargerId = route.params['smsChargerId'] ? route.params['smsChargerId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsDevice>) => response.ok),
                map((smsDevice: HttpResponse<SmsDevice>) => smsDevice.body)
            );
        } else if (smsChargerId) {
            return of({ smsCharger: { id: smsChargerId } } as ISmsDevice);
        }
        return of(null);
    }
}

export const smsDeviceRoute: Routes = [
    {
        path: '',
        component: SmsDeviceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsDeviceDetailComponent,
        resolve: {
            smsDevice: SmsDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:smsChargerId',
        component: SmsDeviceUpdateComponent,
        resolve: {
            smsDevice: SmsDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDevices'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmsDeviceUpdateComponent,
        resolve: {
            smsDevice: SmsDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDevices'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smsDevicePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmsDeviceDeletePopupComponent,
        resolve: {
            smsDevice: SmsDeviceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDevices'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
