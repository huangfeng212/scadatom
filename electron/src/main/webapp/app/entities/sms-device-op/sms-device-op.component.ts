import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';
import { AccountService } from 'app/core';
import { SmsDeviceOpService } from './sms-device-op.service';

@Component({
    selector: 'jhi-sms-device-op',
    templateUrl: './sms-device-op.component.html'
})
export class SmsDeviceOpComponent implements OnInit, OnDestroy {
    smsDeviceOps: ISmsDeviceOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsDeviceOpService: SmsDeviceOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsDeviceOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsDeviceOp[]>) => res.ok),
                map((res: HttpResponse<ISmsDeviceOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmsDeviceOp[]) => {
                    this.smsDeviceOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsDeviceOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsDeviceOp) {
        return item.id;
    }

    registerChangeInSmsDeviceOps() {
        this.eventSubscriber = this.eventManager.subscribe('smsDeviceOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
