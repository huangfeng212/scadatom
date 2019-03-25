import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmBond } from 'app/shared/model/smm-bond.model';

@Component({
    selector: 'jhi-smm-bond-detail',
    templateUrl: './smm-bond-detail.component.html'
})
export class SmmBondDetailComponent implements OnInit {
    smmBond: ISmmBond;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmBond }) => {
            this.smmBond = smmBond;
        });
    }

    previousState() {
        window.history.back();
    }
}
