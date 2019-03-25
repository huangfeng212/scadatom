import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmmCharger } from 'app/shared/model/smm-charger.model';
import { SmmChargerService } from './smm-charger.service';
import { SmmChargerComponent } from './smm-charger.component';
import { SmmChargerDetailComponent } from './smm-charger-detail.component';
import { SmmChargerUpdateComponent } from './smm-charger-update.component';
import { SmmChargerDeletePopupComponent } from './smm-charger-delete-dialog.component';
import { ISmmCharger } from 'app/shared/model/smm-charger.model';

@Injectable({ providedIn: 'root' })
export class SmmChargerResolve implements Resolve<ISmmCharger> {
    constructor(private service: SmmChargerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmCharger> {
        const id = route.params['id'] ? route.params['id'] : null;
        const electronId = route.params['electronId'] ? route.params['electronId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmCharger>) => response.ok),
                map((smmCharger: HttpResponse<SmmCharger>) => smmCharger.body)
            );
        } else if (electronId) {
            return of({ electron: { id: electronId } } as ISmmCharger);
        }
        return of(null);
    }
}

export const smmChargerRoute: Routes = [
    {
        path: '',
        component: SmmChargerComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmChargerDetailComponent,
        resolve: {
            smmCharger: SmmChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:electronId',
        component: SmmChargerUpdateComponent,
        resolve: {
            smmCharger: SmmChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmmChargerUpdateComponent,
        resolve: {
            smmCharger: SmmChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smmChargerPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmmChargerDeletePopupComponent,
        resolve: {
            smmCharger: SmmChargerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
