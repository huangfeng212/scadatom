import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsBond } from 'app/shared/model/sms-bond.model';

@Component({
    selector: 'jhi-sms-bond-detail',
    templateUrl: './sms-bond-detail.component.html'
})
export class SmsBondDetailComponent implements OnInit {
    smsBond: ISmsBond;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsBond }) => {
            this.smsBond = smsBond;
        });
    }

    previousState() {
        window.history.back();
    }
}
