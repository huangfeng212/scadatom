import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';
import { AccountService } from 'app/core';
import { SmsBondOpService } from './sms-bond-op.service';

@Component({
    selector: 'jhi-sms-bond-op',
    templateUrl: './sms-bond-op.component.html'
})
export class SmsBondOpComponent implements OnInit, OnDestroy {
    smsBondOps: ISmsBondOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsBondOpService: SmsBondOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsBondOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsBondOp[]>) => res.ok),
                map((res: HttpResponse<ISmsBondOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmsBondOp[]) => {
                    this.smsBondOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsBondOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsBondOp) {
        return item.id;
    }

    registerChangeInSmsBondOps() {
        this.eventSubscriber = this.eventManager.subscribe('smsBondOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
