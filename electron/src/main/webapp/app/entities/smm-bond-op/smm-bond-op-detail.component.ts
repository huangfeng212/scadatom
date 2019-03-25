import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';

@Component({
    selector: 'jhi-smm-bond-op-detail',
    templateUrl: './smm-bond-op-detail.component.html'
})
export class SmmBondOpDetailComponent implements OnInit {
    smmBondOp: ISmmBondOp;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmBondOp }) => {
            this.smmBondOp = smmBondOp;
        });
    }

    previousState() {
        window.history.back();
    }
}
