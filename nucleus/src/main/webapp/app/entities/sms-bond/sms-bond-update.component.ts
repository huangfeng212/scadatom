import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmsBond } from 'app/shared/model/sms-bond.model';
import { SmsBondService } from './sms-bond.service';
import { IParticle } from 'app/shared/model/particle.model';
import { ParticleService } from 'app/entities/particle';
import { ISmsDevice } from 'app/shared/model/sms-device.model';
import { SmsDeviceService } from 'app/entities/sms-device';

@Component({
    selector: 'jhi-sms-bond-update',
    templateUrl: './sms-bond-update.component.html'
})
export class SmsBondUpdateComponent implements OnInit {
    smsBond: ISmsBond;
    isSaving: boolean;
    isNew: boolean;

    particles: IParticle[];

    smsDevice: ISmsDevice;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smsBondService: SmsBondService,
        protected particleService: ParticleService,
        protected smsDeviceService: SmsDeviceService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smsBond }) => {
            this.smsBond = smsBond;
            this.isNew = !this.smsBond.id;
            this.smsDeviceService
                .find(this.smsBond.smsDevice.id)
                .pipe(
                    filter((mayBeOk: HttpResponse<ISmsDevice>) => mayBeOk.ok),
                    map((response: HttpResponse<ISmsDevice>) => response.body)
                )
                .subscribe(
                    (res: ISmsDevice) => {
                        this.smsDevice = res;
                        this.particleService
                            .query({
                                filter: 'smsbond-is-null-and-in-smsdevice',
                                optId: this.smsBond.smsDevice.id
                            })
                            .pipe(
                                filter((mayBeOk: HttpResponse<IParticle[]>) => mayBeOk.ok),
                                map((response: HttpResponse<IParticle[]>) => response.body)
                            )
                            .subscribe(
                                (res1: IParticle[]) => {
                                    this.particles = res1;
                                },
                                (res1: HttpErrorResponse) => this.onError(res1.message)
                            );
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.isNew) {
            this.subscribeToSaveResponse(this.smsBondService.create(this.smsBond));
        } else {
            this.subscribeToSaveResponse(this.smsBondService.update(this.smsBond));
        }
    }

    trackParticleById(index: number, item: IParticle) {
        return item.id;
    }

    trackSmsDeviceById(index: number, item: ISmsDevice) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmsBond>>) {
        result.subscribe((res: HttpResponse<ISmsBond>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
