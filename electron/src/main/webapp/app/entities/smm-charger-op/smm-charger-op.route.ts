import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmmChargerOp } from 'app/shared/model/smm-charger-op.model';
import { SmmChargerOpService } from './smm-charger-op.service';
import { SmmChargerOpComponent } from './smm-charger-op.component';
import { SmmChargerOpDetailComponent } from './smm-charger-op-detail.component';
import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';

@Injectable({ providedIn: 'root' })
export class SmmChargerOpResolve implements Resolve<ISmmChargerOp> {
    constructor(private service: SmmChargerOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmChargerOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmChargerOp>) => response.ok),
                map((smmChargerOp: HttpResponse<SmmChargerOp>) => smmChargerOp.body)
            );
        }
        return of(new SmmChargerOp());
    }
}

export const smmChargerOpRoute: Routes = [
    {
        path: '',
        component: SmmChargerOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargerOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmChargerOpDetailComponent,
        resolve: {
            smmChargerOp: SmmChargerOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmChargerOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
