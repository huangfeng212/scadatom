import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IElectron } from 'app/shared/model/electron.model';
import { ElectronService } from './electron.service';

@Component({
    selector: 'jhi-electron-update',
    templateUrl: './electron-update.component.html'
})
export class ElectronUpdateComponent implements OnInit {
    electron: IElectron;
    isSaving: boolean;
    isNew: boolean;

    constructor(protected electronService: ElectronService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ electron }) => {
            this.electron = electron;
            this.isNew = !this.electron.id;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.isNew) {
            this.subscribeToSaveResponse(this.electronService.create(this.electron));
        } else {
            this.subscribeToSaveResponse(this.electronService.update(this.electron));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IElectron>>) {
        result.subscribe((res: HttpResponse<IElectron>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
