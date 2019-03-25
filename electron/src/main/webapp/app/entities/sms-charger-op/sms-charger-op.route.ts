import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmsChargerOp } from 'app/shared/model/sms-charger-op.model';
import { SmsChargerOpService } from './sms-charger-op.service';
import { SmsChargerOpComponent } from './sms-charger-op.component';
import { SmsChargerOpDetailComponent } from './sms-charger-op-detail.component';
import { SmsChargerOpUpdateComponent } from './sms-charger-op-update.component';
import { SmsChargerOpDeletePopupComponent } from './sms-charger-op-delete-dialog.component';
import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';

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
    },
    {
        path: 'new',
        component: SmsChargerOpUpdateComponent,
        resolve: {
            smsChargerOp: SmsChargerOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargerOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmsChargerOpUpdateComponent,
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

export const smsChargerOpPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmsChargerOpDeletePopupComponent,
        resolve: {
            smsChargerOp: SmsChargerOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargerOps'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
