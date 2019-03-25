import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IParticle, Particle } from 'app/shared/model/particle.model';
import { ParticleService } from './particle.service';
import { ParticleComponent } from './particle.component';
import { ParticleDetailComponent } from './particle-detail.component';
import { ParticleUpdateComponent } from './particle-update.component';
import { ParticleDeletePopupComponent } from './particle-delete-dialog.component';

@Injectable({ providedIn: 'root' })
export class ParticleResolve implements Resolve<IParticle> {
    constructor(private service: ParticleService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IParticle> {
        const id = route.params['id'] ? route.params['id'] : null;
        const electronId = route.params['electronId'] ? route.params['electronId'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Particle>) => response.ok),
                map((particle: HttpResponse<Particle>) => particle.body)
            );
        } else if (electronId) {
            return of({ electron: { id: electronId } } as IParticle);
        }
        return of(null);
    }
}

export const particleRoute: Routes = [
    {
        path: '',
        component: ParticleComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Particles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: ParticleDetailComponent,
        resolve: {
            particle: ParticleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Particles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new/:electronId',
        component: ParticleUpdateComponent,
        resolve: {
            particle: ParticleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Particles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: ParticleUpdateComponent,
        resolve: {
            particle: ParticleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Particles'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const particlePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: ParticleDeletePopupComponent,
        resolve: {
            particle: ParticleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Particles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
