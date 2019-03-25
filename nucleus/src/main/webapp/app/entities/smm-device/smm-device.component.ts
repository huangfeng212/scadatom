import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmmDevice } from 'app/shared/model/smm-device.model';
import { AccountService } from 'app/core';
import { SmmDeviceService } from './smm-device.service';

@Component({
    selector: 'jhi-smm-device',
    templateUrl: './smm-device.component.html'
})
export class SmmDeviceComponent implements OnInit, OnDestroy {
    smmDevices: ISmmDevice[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmDeviceService: SmmDeviceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmDeviceService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmDevice[]>) => res.ok),
                map((res: HttpResponse<ISmmDevice[]>) => res.body)
            )
            .subscribe(
                (res: ISmmDevice[]) => {
                    this.smmDevices = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmDevices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmDevice) {
        return item.id;
    }

    registerChangeInSmmDevices() {
        this.eventSubscriber = this.eventManager.subscribe('smmDeviceListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
