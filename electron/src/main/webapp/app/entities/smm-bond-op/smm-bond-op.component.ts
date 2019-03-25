import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';
import { AccountService } from 'app/core';
import { SmmBondOpService } from './smm-bond-op.service';

@Component({
    selector: 'jhi-smm-bond-op',
    templateUrl: './smm-bond-op.component.html'
})
export class SmmBondOpComponent implements OnInit, OnDestroy {
    smmBondOps: ISmmBondOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmBondOpService: SmmBondOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmBondOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmBondOp[]>) => res.ok),
                map((res: HttpResponse<ISmmBondOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmmBondOp[]) => {
                    this.smmBondOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmBondOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmBondOp) {
        return item.id;
    }

    registerChangeInSmmBondOps() {
        this.eventSubscriber = this.eventManager.subscribe('smmBondOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
