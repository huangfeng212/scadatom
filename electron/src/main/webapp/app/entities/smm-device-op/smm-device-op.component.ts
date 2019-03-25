import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';
import { AccountService } from 'app/core';
import { SmmDeviceOpService } from './smm-device-op.service';

@Component({
    selector: 'jhi-smm-device-op',
    templateUrl: './smm-device-op.component.html'
})
export class SmmDeviceOpComponent implements OnInit, OnDestroy {
    smmDeviceOps: ISmmDeviceOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmDeviceOpService: SmmDeviceOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmDeviceOpService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmDeviceOp[]>) => res.ok),
                map((res: HttpResponse<ISmmDeviceOp[]>) => res.body)
            )
            .subscribe(
                (res: ISmmDeviceOp[]) => {
                    this.smmDeviceOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmDeviceOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmDeviceOp) {
        return item.id;
    }

    registerChangeInSmmDeviceOps() {
        this.eventSubscriber = this.eventManager.subscribe('smmDeviceOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
