import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISmmCharger } from 'app/shared/model/smm-charger.model';
import { SmmChargerService } from './smm-charger.service';
import { IElectron } from 'app/shared/model/electron.model';
import { ElectronService } from 'app/entities/electron';

@Component({
    selector: 'jhi-smm-charger-update',
    templateUrl: './smm-charger-update.component.html'
})
export class SmmChargerUpdateComponent implements OnInit {
    smmCharger: ISmmCharger;
    isSaving: boolean;
    isNew: boolean;

    electron: IElectron;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected smmChargerService: SmmChargerService,
        protected electronService: ElectronService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ smmCharger }) => {
            this.smmCharger = smmCharger;
            this.isNew = !this.smmCharger.id;
            this.electronService
                .find(this.smmCharger.electron.id)
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
            this.subscribeToSaveResponse(this.smmChargerService.create(this.smmCharger));
        } else {
            this.subscribeToSaveResponse(this.smmChargerService.update(this.smmCharger));
        }
    }

    trackElectronById(index: number, item: IElectron) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISmmCharger>>) {
        result.subscribe((res: HttpResponse<ISmmCharger>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
