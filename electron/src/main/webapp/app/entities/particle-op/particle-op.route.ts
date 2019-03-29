import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ParticleOp } from 'app/shared/model/particle-op.model';
import { ParticleOpService } from './particle-op.service';
import { ParticleOpComponent } from './particle-op.component';
import { ParticleOpDetailComponent } from './particle-op-detail.component';
import { IParticleOp } from 'app/shared/model/particle-op.model';

@Injectable({ providedIn: 'root' })
export class ParticleOpResolve implements Resolve<IParticleOp> {
    constructor(private service: ParticleOpService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParticleOp> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<ParticleOp>) => response.ok),
                map((particleOp: HttpResponse<ParticleOp>) => particleOp.body)
            );
        }
        return of(new ParticleOp());
    }
}

export const particleOpRoute: Routes = [
    {
        path: '',
        component: ParticleOpComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ParticleOps'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ParticleOpDetailComponent,
        resolve: {
            particleOp: ParticleOpResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ParticleOps'
        },
        canActivate: [UserRouteAccessService]
    }
];
