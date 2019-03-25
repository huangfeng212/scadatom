import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmmBondOp } from 'app/shared/model/smm-bond-op.model';
import { SmmBondOpService } from './smm-bond-op.service';
import { SmmBondOpComponent } from './smm-bond-op.component';
import { SmmBondOpDetailComponent } from './smm-bond-op-detail.component';
import { SmmBondOpUpdateComponent } from './smm-bond-op-update.component';
import { SmmBondOpDeletePopupComponent } from './smm-bond-op-delete-dialog.component';
import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';

@Injectable({ providedIn: 'root' })
export class SmmBondOpResolve implements Resolve<ISmmBondOp> {
    constructor(private service: SmmBondOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmBondOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmBondOp>) => response.ok),
                map((smmBondOp: HttpResponse<SmmBondOp>) => smmBondOp.body)
            );
        }
        return of(new SmmBondOp());
    }
}

export const smmBondOpRoute: Routes = [
    {
        path: '',
        component: SmmBondOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBondOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmBondOpDetailComponent,
        resolve: {
            smmBondOp: SmmBondOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBondOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SmmBondOpUpdateComponent,
        resolve: {
            smmBondOp: SmmBondOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBondOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SmmBondOpUpdateComponent,
        resolve: {
            smmBondOp: SmmBondOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBondOps'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const smmBondOpPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SmmBondOpDeletePopupComponent,
        resolve: {
            smmBondOp: SmmBondOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmBondOps'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
