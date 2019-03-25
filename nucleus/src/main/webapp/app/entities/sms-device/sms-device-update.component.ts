import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmsDevice } from 'app/shared/model/sms-device.model';
import { SmsDeviceService } from './sms-device.service';
import { ISmsCharger } from 'app/shared/model/sms-charger.model';
import { SmsChargerService } from 'app/entities/sms-charger';

@Component({
    selector: 'jhi-sms-device-update',
    templateUrl: './sms-device-update.component.html'
})
export class SmsDeviceUpdateComponent implements OnInit {
    smsDevice: ISmsDevice;
    isSaving: boolean;
    isNew: boolean;

    smsCharger: ISmsCharger;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smsDeviceService: SmsDeviceService,
        protected smsChargerService: SmsChargerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsDevice }) => {
            this.smsDevice = smsDevice;
            this.isNew = !this.smsDevice.id;
            this.smsChargerService
                .find(this.smsDevice.smsCharger.id)
                .pipe(
                    filter((mayBeOk: HttpResponse<ISmsCharger>) => mayBeOk.ok),
                    map((response: HttpResponse<ISmsCharger>) => response.body)
                )
                .subscribe((res: ISmsCharger) => (this.smsCharger = res), (res: HttpErrorResponse) => this.onError(res.message));
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.isNew) {
            this.subscribeToSaveResponse(this.smsDeviceService.create(this.smsDevice));
        } else {
            this.subscribeToSaveResponse(this.smsDeviceService.update(this.smsDevice));
        }
    }

    trackSmsChargerById(index: number, item: ISmsCharger) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsDevice>>) {
        result.subscribe((res: HttpResponse<ISmsDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
