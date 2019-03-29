import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SmsBondOp } from 'app/shared/model/sms-bond-op.model';
import { SmsBondOpService } from './sms-bond-op.service';
import { SmsBondOpComponent } from './sms-bond-op.component';
import { SmsBondOpDetailComponent } from './sms-bond-op-detail.component';
import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';

@Injectable({ providedIn: 'root' })
export class SmsBondOpResolve implements Resolve<ISmsBondOp> {
    constructor(private service: SmsBondOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISmsBondOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SmsBondOp>) => response.ok),
                map((smsBondOp: HttpResponse<SmsBondOp>) => smsBondOp.body)
            );
        }
        return of(new SmsBondOp());
    }
}

export const smsBondOpRoute: Routes = [
    {
        path: '',
        component: SmsBondOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBondOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SmsBondOpDetailComponent,
        resolve: {
            smsBondOp: SmsBondOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SmsBondOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
