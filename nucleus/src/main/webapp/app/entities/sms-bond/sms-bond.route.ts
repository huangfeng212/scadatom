import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmsBond } from 'app/shared/model/sms-bond.model';
import { SmsBondService } from './sms-bond.service';
import { SmsBondComponent } from './sms-bond.component';
import { SmsBondDetailComponent } from './sms-bond-detail.component';
import { SmsBondUpdateComponent } from './sms-bond-update.component';
import { SmsBondDeletePopupComponent } from './sms-bond-delete-dialog.component';
import { ISmsBond } from 'app/shared/model/sms-bond.model';

@Injectable({ providedIn: 'root' })
export class SmsBondResolve implements Resolve<ISmsBond> {
    constructor(private service: SmsBondService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsBond> {
        const id = route.params['id'] ? route.params['id'] : null;
        const smsDeviceId = route.params['smsDeviceId'] ? route.params['smsDeviceId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsBond>) => response.ok),
                map((smsBond: HttpResponse<SmsBond>) => smsBond.body)
            );
        } else if (smsDeviceId) {
            return of({ smsDevice: { id: smsDeviceId } } as ISmsBond);
        }
        return of(null);
    }
}

export const smsBondRoute: Routes = [
    {
        path: '',
        component: SmsBondComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsBondDetailComponent,
        resolve: {
            smsBond: SmsBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:smsDeviceId',
        component: SmsBondUpdateComponent,
        resolve: {
            smsBond: SmsBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmsBondUpdateComponent,
        resolve: {
            smsBond: SmsBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBonds'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smsBondPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmsBondDeletePopupComponent,
        resolve: {
            smsBond: SmsBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBonds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
