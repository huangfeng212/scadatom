import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmsDeviceOp } from 'app/shared/model/sms-device-op.model';
import { SmsDeviceOpService } from './sms-device-op.service';
import { SmsDeviceOpComponent } from './sms-device-op.component';
import { SmsDeviceOpDetailComponent } from './sms-device-op-detail.component';
import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';

@Injectable({ providedIn: 'root' })
export class SmsDeviceOpResolve implements Resolve<ISmsDeviceOp> {
    constructor(private service: SmsDeviceOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsDeviceOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsDeviceOp>) => response.ok),
                map((smsDeviceOp: HttpResponse<SmsDeviceOp>) => smsDeviceOp.body)
            );
        }
        return of(new SmsDeviceOp());
    }
}

export const smsDeviceOpRoute: Routes = [
    {
        path: '',
        component: SmsDeviceOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDeviceOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsDeviceOpDetailComponent,
        resolve: {
            smsDeviceOp: SmsDeviceOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsDeviceOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
