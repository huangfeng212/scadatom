import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmmDevice } from 'app/shared/model/smm-device.model';
import { SmmDeviceService } from './smm-device.service';
import { ISmmCharger } from 'app/shared/model/smm-charger.model';
import { SmmChargerService } from 'app/entities/smm-charger';

@Component({
    selector: 'jhi-smm-device-update',
    templateUrl: './smm-device-update.component.html'
})
export class SmmDeviceUpdateComponent implements OnInit {
    smmDevice: ISmmDevice;
    isSaving: boolean;
    isNew: boolean;

    smmCharger: ISmmCharger;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smmDeviceService: SmmDeviceService,
        protected smmChargerService: SmmChargerService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmDevice }) => {
            this.smmDevice = smmDevice;
            this.isNew = !this.smmDevice.id;
            this.smmChargerService
                .find(this.smmDevice.smmCharger.id)
                .pipe(
                    filter((mayBeOk: HttpResponse<ISmmCharger>) => mayBeOk.ok),
                    map((response: HttpResponse<ISmmCharger>) => response.body)
                )
                .subscribe((res: ISmmCharger) => (this.smmCharger = res), (res: HttpErrorResponse) => this.onError(res.message));
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.isNew) {
            this.subscribeToSaveResponse(this.smmDeviceService.create(this.smmDevice));
        } else {
            this.subscribeToSaveResponse(this.smmDeviceService.update(this.smmDevice));
        }
    }

    trackSmmChargerById(index: number, item: ISmmCharger) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmDevice>>) {
        result.subscribe((res: HttpResponse<ISmmDevice>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
