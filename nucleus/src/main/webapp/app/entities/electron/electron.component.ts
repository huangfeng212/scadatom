import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IElectron } from 'app/shared/model/electron.model';
import { AccountService } from 'app/core';
import { ElectronService } from './electron.service';

@Component({
    selector: 'jhi-electron',
    templateUrl: './electron.component.html'
})
export class ElectronComponent implements OnInit, OnDestroy {
    electrons: IElectron[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected electronService: ElectronService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.electronService
            .query()
            .pipe(
                filter((res: HttpResponse<IElectron[]>) => res.ok),
                map((res: HttpResponse<IElectron[]>) => res.body)
            )
            .subscribe(
                (res: IElectron[]) => {
                    this.electrons = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInElectrons();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IElectron) {
        return item.id;
    }

    registerChangeInElectrons() {
        this.eventSubscriber = this.eventManager.subscribe('electronListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
