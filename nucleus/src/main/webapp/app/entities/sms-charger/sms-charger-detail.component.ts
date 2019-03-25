import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsCharger } from 'app/shared/model/sms-charger.model';

@Component({
    selector: 'jhi-sms-charger-detail',
    templateUrl: './sms-charger-detail.component.html'
})
export class SmsChargerDetailComponent implements OnInit {
    smsCharger: ISmsCharger;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsCharger }) => {
            this.smsCharger = smsCharger;
        });
    }

    previousState() {
        window.history.back();
    }
}
