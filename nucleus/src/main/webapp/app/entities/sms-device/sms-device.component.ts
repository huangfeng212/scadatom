import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsDevice } from 'app/shared/model/sms-device.model';
import { AccountService } from 'app/core';
import { SmsDeviceService } from './sms-device.service';

@Component({
    selector: 'jhi-sms-device',
    templateUrl: './sms-device.component.html'
})
export class SmsDeviceComponent implements OnInit, OnDestroy {
    smsDevices: ISmsDevice[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsDeviceService: SmsDeviceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsDeviceService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsDevice[]>) => res.ok),
                map((res: HttpResponse<ISmsDevice[]>) => res.body)
            )
            .subscribe(
                (res: ISmsDevice[]) => {
                    this.smsDevices = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsDevices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsDevice) {
        return item.id;
    }

    registerChangeInSmsDevices() {
        this.eventSubscriber = this.eventManager.subscribe('smsDeviceListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
