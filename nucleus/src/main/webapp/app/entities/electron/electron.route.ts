import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Electron } from 'app/shared/model/electron.model';
import { ElectronService } from './electron.service';
import { ElectronComponent } from './electron.component';
import { ElectronDetailComponent } from './electron-detail.component';
import { ElectronUpdateComponent } from './electron-update.component';
import { ElectronDeletePopupComponent } from './electron-delete-dialog.component';
import { IElectron } from 'app/shared/model/electron.model';

@Injectable({ providedIn: 'root' })
export class ElectronResolve implements Resolve<IElectron> {
    constructor(private service: ElectronService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IElectron> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Electron>) => response.ok),
                map((electron: HttpResponse<Electron>) => electron.body)
            );
        }
        return of(new Electron());
    }
}

export const electronRoute: Routes = [
    {
        path: '',
        component: ElectronComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Electrons'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ElectronDetailComponent,
        resolve: {
            electron: ElectronResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Electrons'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: ElectronUpdateComponent,
        resolve: {
            electron: ElectronResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Electrons'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ElectronUpdateComponent,
        resolve: {
            electron: ElectronResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Electrons'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const electronPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ElectronDeletePopupComponent,
        resolve: {
            electron: ElectronResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Electrons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
