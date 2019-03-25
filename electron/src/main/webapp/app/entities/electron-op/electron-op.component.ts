import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IElectronOp } from 'app/shared/model/electron-op.model';
import { AccountService } from 'app/core';
import { ElectronOpService } from './electron-op.service';

@Component({
    selector: 'jhi-electron-op',
    templateUrl: './electron-op.component.html'
})
export class ElectronOpComponent implements OnInit, OnDestroy {
    electronOps: IElectronOp[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected electronOpService: ElectronOpService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.electronOpService
            .query()
            .pipe(
                filter((res: HttpResponse<IElectronOp[]>) => res.ok),
                map((res: HttpResponse<IElectronOp[]>) => res.body)
            )
            .subscribe(
                (res: IElectronOp[]) => {
                    this.electronOps = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInElectronOps();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IElectronOp) {
        return item.id;
    }

    registerChangeInElectronOps() {
        this.eventSubscriber = this.eventManager.subscribe('electronOpListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
