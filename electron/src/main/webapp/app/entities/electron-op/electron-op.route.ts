import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ElectronOp } from 'app/shared/model/electron-op.model';
import { ElectronOpService } from './electron-op.service';
import { ElectronOpComponent } from './electron-op.component';
import { ElectronOpDetailComponent } from './electron-op-detail.component';
import { ElectronOpUpdateComponent } from './electron-op-update.component';
import { ElectronOpDeletePopupComponent } from './electron-op-delete-dialog.component';
import { IElectronOp } from 'app/shared/model/electron-op.model';

@Injectable({ providedIn: 'root' })
export class ElectronOpResolve implements Resolve<IElectronOp> {
    constructor(private service: ElectronOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IElectronOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ElectronOp>) => response.ok),
                map((electronOp: HttpResponse<ElectronOp>) => electronOp.body)
            );
        }
        return of(new ElectronOp());
    }
}

export const electronOpRoute: Routes = [
    {
        path: '',
        component: ElectronOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ElectronOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ElectronOpDetailComponent,
        resolve: {
            electronOp: ElectronOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ElectronOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ElectronOpUpdateComponent,
        resolve: {
            electronOp: ElectronOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ElectronOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ElectronOpUpdateComponent,
        resolve: {
            electronOp: ElectronOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ElectronOps'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const electronOpPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ElectronOpDeletePopupComponent,
        resolve: {
            electronOp: ElectronOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ElectronOps'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
