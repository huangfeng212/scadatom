import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsDevice } from 'app/shared/model/sms-device.model';

@Component({
    selector: 'jhi-sms-device-detail',
    templateUrl: './sms-device-detail.component.html'
})
export class SmsDeviceDetailComponent implements OnInit {
    smsDevice: ISmsDevice;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsDevice }) => {
            this.smsDevice = smsDevice;
        });
    }

    previousState() {
        window.history.back();
    }
}
