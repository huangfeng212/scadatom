import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmmBond } from 'app/shared/model/smm-bond.model';
import { SmmBondService } from './smm-bond.service';
import { IParticle } from 'app/shared/model/particle.model';
import { ParticleService } from 'app/entities/particle';
import { ISmmDevice } from 'app/shared/model/smm-device.model';
import { SmmDeviceService } from 'app/entities/smm-device';

@Component({
    selector: 'jhi-smm-bond-update',
    templateUrl: './smm-bond-update.component.html'
})
export class SmmBondUpdateComponent implements OnInit {
    smmBond: ISmmBond;
    isSaving: boolean;
    isNew: boolean;

    particles: IParticle[];

    smmDevice: ISmmDevice;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smmBondService: SmmBondService,
        protected particleService: ParticleService,
        protected smmDeviceService: SmmDeviceService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmBond }) => {
            this.smmBond = smmBond;
            this.isNew = !this.smmBond.id;
            this.smmDeviceService
                .find(this.smmBond.smmDevice.id)
                .pipe(
                    filter((mayBeOk: HttpResponse<ISmmDevice>) => mayBeOk.ok),
                    map((response: HttpResponse<ISmmDevice>) => response.body)
                )
                .subscribe(
                    (res: ISmmDevice) => {
                        this.smmDevice = res;
                        this.particleService
                            .query({
                                filter: 'smmbond-is-null-and-in-smmdevice',
                                optId: this.smmBond.smmDevice.id
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
            this.subscribeToSaveResponse(this.smmBondService.create(this.smmBond));
        } else {
            this.subscribeToSaveResponse(this.smmBondService.update(this.smmBond));
        }
    }

    trackParticleById(index: number, item: IParticle) {
        return item.id;
    }

    trackSmmDeviceById(index: number, item: ISmmDevice) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmBond>>) {
        result.subscribe((res: HttpResponse<ISmmBond>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
