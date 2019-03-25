import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';

@Component({
    selector: 'jhi-sms-device-op-detail',
    templateUrl: './sms-device-op-detail.component.html'
})
export class SmsDeviceOpDetailComponent implements OnInit {
    smsDeviceOp: ISmsDeviceOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsDeviceOp }) => {
            this.smsDeviceOp = smsDeviceOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
