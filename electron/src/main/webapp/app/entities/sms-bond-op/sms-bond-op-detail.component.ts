import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';

@Component({
    selector: 'jhi-sms-bond-op-detail',
    templateUrl: './sms-bond-op-detail.component.html'
})
export class SmsBondOpDetailComponent implements OnInit {
    smsBondOp: ISmsBondOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsBondOp }) => {
            this.smsBondOp = smsBondOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
