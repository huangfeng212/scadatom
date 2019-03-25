import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISmsBond } from 'app/shared/model/sms-bond.model';
import { AccountService } from 'app/core';
import { SmsBondService } from './sms-bond.service';

@Component({
    selector: 'jhi-sms-bond',
    templateUrl: './sms-bond.component.html'
})
export class SmsBondComponent implements OnInit, OnDestroy {
    smsBonds: ISmsBond[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected smsBondService: SmsBondService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.smsBondService
            .query()
            .pipe(
                filter((res: HttpResponse<ISmsBond[]>) => res.ok),
                map((res: HttpResponse<ISmsBond[]>) => res.body)
            )
            .subscribe(
                (res: ISmsBond[]) => {
                    this.smsBonds = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSmsBonds();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISmsBond) {
        return item.id;
    }

    registerChangeInSmsBonds() {
        this.eventSubscriber = this.eventManager.subscribe('smsBondListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
