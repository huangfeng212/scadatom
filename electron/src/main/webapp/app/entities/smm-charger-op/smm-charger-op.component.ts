import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';
import { AccountService } from 'app/core';
import { SmmChargerOpService } from './smm-charger-op.service';

@Component({
    selector: 'jhi-smm-charger-op',
    templateUrl: './smm-charger-op.component.html'
})
export class SmmChargerOpComponent implements OnInit, OnDestroy {
    smmChargerOps: ISmmChargerOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmChargerOpService: SmmChargerOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmChargerOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmChargerOp[]>) => res.ok),
                map((res: HttpResponse<ISmmChargerOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmmChargerOp[]) => {
                    this.smmChargerOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmChargerOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmChargerOp) {
        return item.id;
    }

    registerChangeInSmmChargerOps() {
        this.eventSubscriber = this.eventManager.subscribe('smmChargerOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
