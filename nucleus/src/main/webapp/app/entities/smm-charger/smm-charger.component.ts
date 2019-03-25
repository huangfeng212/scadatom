import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmmCharger } from 'app/shared/model/smm-charger.model';
import { AccountService } from 'app/core';
import { SmmChargerService } from './smm-charger.service';

@Component({
    selector: 'jhi-smm-charger',
    templateUrl: './smm-charger.component.html'
})
export class SmmChargerComponent implements OnInit, OnDestroy {
    smmChargers: ISmmCharger[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smmChargerService: SmmChargerService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smmChargerService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmmCharger[]>) => res.ok),
                map((res: HttpResponse<ISmmCharger[]>) => res.body)
            )
            .subscribe(
                (res: ISmmCharger[]) => {
                    this.smmChargers = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmmChargers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmmCharger) {
        return item.id;
    }

    registerChangeInSmmChargers() {
        this.eventSubscriber = this.eventManager.subscribe('smmChargerListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
