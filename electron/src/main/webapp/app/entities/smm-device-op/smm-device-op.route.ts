import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmmDeviceOp } from 'app/shared/model/smm-device-op.model';
import { SmmDeviceOpService } from './smm-device-op.service';
import { SmmDeviceOpComponent } from './smm-device-op.component';
import { SmmDeviceOpDetailComponent } from './smm-device-op-detail.component';
import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';

@Injectable({ providedIn: 'root' })
export class SmmDeviceOpResolve implements Resolve<ISmmDeviceOp> {
    constructor(private service: SmmDeviceOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmmDeviceOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmmDeviceOp>) => response.ok),
                map((smmDeviceOp: HttpResponse<SmmDeviceOp>) => smmDeviceOp.body)
            );
        }
        return of(new SmmDeviceOp());
    }
}

export const smmDeviceOpRoute: Routes = [
    {
        path: '',
        component: SmmDeviceOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDeviceOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmmDeviceOpDetailComponent,
        resolve: {
            smmDeviceOp: SmmDeviceOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmmDeviceOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
