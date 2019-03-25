import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISmsCharger, SmsCharger } from 'app/shared/model/sms-charger.model';
import { SmsChargerService } from './sms-charger.service';
import { SmsChargerComponent } from './sms-charger.component';
import { SmsChargerDetailComponent } from './sms-charger-detail.component';
import { SmsChargerUpdateComponent } from './sms-charger-update.component';
import { SmsChargerDeletePopupComponent } from './sms-charger-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SmsChargerResolve implements Resolve<ISmsCharger> {
    constructor(private service: SmsChargerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsCharger> {
        const id = route.params['id'] ? route.params['id'] : null;
        const electronId = route.params['electronId'] ? route.params['electronId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsCharger>) => response.ok),
                map((smsCharger: HttpResponse<SmsCharger>) => smsCharger.body)
            );
        } else if (electronId) {
            return of({ electron: { id: electronId } } as SmsCharger);
        }
        return of(null);
    }
}

export const smsChargerRoute: Routes = [
    {
        path: '',
        component: SmsChargerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsChargerDetailComponent,
        resolve: {
            smsCharger: SmsChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:electronId',
        component: SmsChargerUpdateComponent,
        resolve: {
            smsCharger: SmsChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmsChargerUpdateComponent,
        resolve: {
            smsCharger: SmsChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smsChargerPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmsChargerDeletePopupComponent,
        resolve: {
            smsCharger: SmsChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsChargers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
