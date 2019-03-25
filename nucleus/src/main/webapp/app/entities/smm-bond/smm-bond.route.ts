import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ISmmBond, SmmBond } from 'app/shared/model/smm-bond.model';
import { SmmBondService } from './smm-bond.service';
import { SmmBondComponent } from './smm-bond.component';
import { SmmBondDetailComponent } from './smm-bond-detail.component';
import { SmmBondUpdateComponent } from './smm-bond-update.component';
import { SmmBondDeletePopupComponent } from './smm-bond-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class SmmBondResolve implements Resolve<ISmmBond> {
    constructor(private service: SmmBondService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmBond> {
        const id = route.params['id'] ? route.params['id'] : null;
        const smmDeviceId = route.params['smmDeviceId'] ? route.params['smmDeviceId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmBond>) => response.ok),
                map((smmBond: HttpResponse<SmmBond>) => smmBond.body)
            );
        } else if (smmDeviceId) {
            return of({ smmDevice: { id: smmDeviceId } } as ISmmBond);
        }
        return of(new SmmBond());
    }
}

export const smmBondRoute: Routes = [
    {
        path: '',
        component: SmmBondComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmBondDetailComponent,
        resolve: {
            smmBond: SmmBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:smmDeviceId',
        component: SmmBondUpdateComponent,
        resolve: {
            smmBond: SmmBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBonds'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmmBondUpdateComponent,
        resolve: {
            smmBond: SmmBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBonds'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smmBondPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmmBondDeletePopupComponent,
        resolve: {
            smmBond: SmmBondResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBonds'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
