import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';

@Component({
    selector: 'jhi-smm-charger-op-detail',
    templateUrl: './smm-charger-op-detail.component.html'
})
export class SmmChargerOpDetailComponent implements OnInit {
    smmChargerOp: ISmmChargerOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmChargerOp }) => {
            this.smmChargerOp = smmChargerOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
