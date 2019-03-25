import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IElectronOp } from 'app/shared/model/electron-op.model';

@Component({
    selector: 'jhi-electron-op-detail',
    templateUrl: './electron-op-detail.component.html'
})
export class ElectronOpDetailComponent implements OnInit {
    electronOp: IElectronOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ electronOp }) => {
            this.electronOp = electronOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
