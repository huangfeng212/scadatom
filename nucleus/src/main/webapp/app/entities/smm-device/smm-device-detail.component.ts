import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmDevice } from 'app/shared/model/smm-device.model';

@Component({
    selector: 'jhi-smm-device-detail',
    templateUrl: './smm-device-detail.component.html'
})
export class SmmDeviceDetailComponent implements OnInit {
    smmDevice: ISmmDevice;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmDevice }) => {
            this.smmDevice = smmDevice;
        });
    }

    previousState() {
        window.history.back();
    }
}
