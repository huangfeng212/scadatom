import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IParticleOp } from 'app/shared/model/particle-op.model';
import { AccountService } from 'app/core';
import { ParticleOpService } from './particle-op.service';

@Component({
    selector: 'jhi-particle-op',
    templateUrl: './particle-op.component.html'
})
export class ParticleOpComponent implements OnInit, OnDestroy {
    particleOps: IParticleOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected particleOpService: ParticleOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.particleOpService
            .query()
            .pipe(
                filter((res: HttpResponse<IParticleOp[]>) => res.ok),
                map((res: HttpResponse<IParticleOp[]>) => res.body)
            )
            .subscribe(
                (res: IParticleOp[]) => {
                    this.particleOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInParticleOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IParticleOp) {
        return item.id;
    }

    registerChangeInParticleOps() {
        this.eventSubscriber = this.eventManager.subscribe('particleOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
