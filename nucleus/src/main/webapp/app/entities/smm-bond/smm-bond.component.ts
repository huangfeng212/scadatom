import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ISmmBond } from 'app/shared/model/smm-bond.model';
import { AccountService } from 'app/core';
import { SmmBondService } from './smm-bond.service';

@Component({
    selector: 'jhi-smm-bond',
    templateUrl: './smm-bond.component.html'
})
export class SmmBondComponent implements OnInit, OnDestroy {
    smmBonds: ISmmBond[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmBondService: SmmBondService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmBondService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmBond[]>) => res.ok),
                map((res: HttpResponse<ISmmBond[]>) => res.body)
            )
            .subscribe(
                (res: ISmmBond[]) => {
                    this.smmBonds = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmBonds();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmBond) {
        return item.id;
    }

    registerChangeInSmmBonds() {
        this.eventSubscriber = this.eventManager.subscribe('smmBondListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
