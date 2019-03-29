import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISmsChargerOp, SmsChargerOp } from 'app/shared/model/sms-charger-op.model';
import { SmsChargerOpService } from './sms-charger-op.service';
import { SmsChargerOpComponent } from './sms-charger-op.component';
import { SmsChargerOpDetailComponent } from './sms-charger-op-detail.component';

@Injectable({ providedIn: 'root' })
export class SmsChargerOpResolve implements Resolve<ISmsChargerOp> {
    constructor(private service: SmsChargerOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsChargerOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsChargerOp>) => response.ok),
                map((smsChargerOp: HttpResponse<SmsChargerOp>) => smsChargerOp.body)
            );
        }
        return of(new SmsChargerOp());
    }
}

export const smsChargerOpRoute: Routes = [
    {
        path: '',
        component: SmsChargerOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargerOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsChargerOpDetailComponent,
        resolve: {
            smsChargerOp: SmsChargerOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargerOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
