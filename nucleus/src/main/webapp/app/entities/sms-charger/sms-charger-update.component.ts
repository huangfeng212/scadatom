import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmsCharger } from 'app/shared/model/sms-charger.model';
import { SmsChargerService } from './sms-charger.service';
import { IElectron } from 'app/shared/model/electron.model';
import { ElectronService } from 'app/entities/electron';

@Component({
    selector: 'jhi-sms-charger-update',
    templateUrl: './sms-charger-update.component.html'
})
export class SmsChargerUpdateComponent implements OnInit {
    smsCharger: ISmsCharger;
    isSaving: boolean;
    isNew: boolean;

    electron: IElectron;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smsChargerService: SmsChargerService,
        protected electronService: ElectronService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsCharger }) => {
            this.smsCharger = smsCharger;
            this.isNew = !this.smsCharger.id;
            this.electronService
                .find(this.smsCharger.electron.id)
                .pipe(
                    filter((mayBeOk: HttpResponse<IElectron>) => mayBeOk.ok),
                    map((response: HttpResponse<IElectron>) => response.body)
                )
                .subscribe((res: IElectron) => (this.electron = res), (res: HttpErrorResponse) => this.onError(res.message));
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.isNew) {
            this.subscribeToSaveResponse(this.smsChargerService.create(this.smsCharger));
        } else {
            this.subscribeToSaveResponse(this.smsChargerService.update(this.smsCharger));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsCharger>>) {
        result.subscribe((res: HttpResponse<ISmsCharger>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackElectronById(index: number, item: IElectron) {
        return item.id;
    }
}
