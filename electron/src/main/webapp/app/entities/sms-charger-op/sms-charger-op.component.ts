import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';
import { AccountService } from 'app/core';
import { SmsChargerOpService } from './sms-charger-op.service';

@Component({
    selector: 'jhi-sms-charger-op',
    templateUrl: './sms-charger-op.component.html'
})
export class SmsChargerOpComponent implements OnInit, OnDestroy {
    smsChargerOps: ISmsChargerOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsChargerOpService: SmsChargerOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsChargerOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsChargerOp[]>) => res.ok),
                map((res: HttpResponse<ISmsChargerOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmsChargerOp[]) => {
                    this.smsChargerOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsChargerOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsChargerOp) {
        return item.id;
    }

    registerChangeInSmsChargerOps() {
        this.eventSubscriber = this.eventManager.subscribe('smsChargerOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
