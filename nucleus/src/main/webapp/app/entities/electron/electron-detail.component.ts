import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IElectron } from 'app/shared/model/electron.model';

@Component({
    selector: 'jhi-electron-detail',
    templateUrl: './electron-detail.component.html'
})
export class ElectronDetailComponent implements OnInit {
    electron: IElectron;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ electron }) => {
            this.electron = electron;
        });
    }

    previousState() {
        window.history.back();
    }
}
