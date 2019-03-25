import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISmmCharger } from 'app/shared/model/smm-charger.model';

@Component({
    selector: 'jhi-smm-charger-detail',
    templateUrl: './smm-charger-detail.component.html'
})
export class SmmChargerDetailComponent implements OnInit {
    smmCharger: ISmmCharger;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmCharger }) => {
            this.smmCharger = smmCharger;
        });
    }

    previousState() {
        window.history.back();
    }
}
