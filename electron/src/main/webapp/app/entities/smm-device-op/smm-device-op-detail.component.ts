import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';

@Component({
    selector: 'jhi-smm-device-op-detail',
    templateUrl: './smm-device-op-detail.component.html'
})
export class SmmDeviceOpDetailComponent implements OnInit {
    smmDeviceOp: ISmmDeviceOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmDeviceOp }) => {
            this.smmDeviceOp = smmDeviceOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
