import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';

@Component({
    selector: 'jhi-sms-charger-op-detail',
    templateUrl: './sms-charger-op-detail.component.html'
})
export class SmsChargerOpDetailComponent implements OnInit {
    smsChargerOp: ISmsChargerOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsChargerOp }) => {
            this.smsChargerOp = smsChargerOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
