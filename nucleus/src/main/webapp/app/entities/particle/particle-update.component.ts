import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IParticle } from 'app/shared/model/particle.model';
import { ParticleService } from './particle.service';
import { IElectron } from 'app/shared/model/electron.model';
import { ElectronService } from 'app/entities/electron';

@Component({
    selector: 'jhi-particle-update',
    templateUrl: './particle-update.component.html'
})
export class ParticleUpdateComponent implements OnInit {
    particle: IParticle;
    isSaving: boolean;
    isNew: boolean;

    electron: IElectron;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected particleService: ParticleService,
        protected electronService: ElectronService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ particle }) => {
            this.particle = particle;
            this.isNew = !this.particle.id;
            this.electronService
                .find(this.particle.electron.id)
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
            this.subscribeToSaveResponse(this.particleService.create(this.particle));
        } else {
            this.subscribeToSaveResponse(this.particleService.update(this.particle));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticle>>) {
        result.subscribe((res: HttpResponse<IParticle>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
