import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsCharger } from 'app/shared/model/sms-charger.model';
import { AccountService } from 'app/core';
import { SmsChargerService } from './sms-charger.service';

@Component({
    selector: 'jhi-sms-charger',
    templateUrl: './sms-charger.component.html'
})
export class SmsChargerComponent implements OnInit, OnDestroy {
    smsChargers: ISmsCharger[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsChargerService: SmsChargerService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsChargerService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsCharger[]>) => res.ok),
                map((res: HttpResponse<ISmsCharger[]>) => res.body)
            )
            .subscribe(
                (res: ISmsCharger[]) => {
                    this.smsChargers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsChargers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsCharger) {
        return item.id;
    }

    registerChangeInSmsChargers() {
        this.eventSubscriber = this.eventManager.subscribe('smsChargerListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
