import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsCharger } from 'app/shared/model/sms-charger.model';
import { SmsChargerService } from './sms-charger.service';

@Component({
    selector: 'jhi-sms-charger-delete-dialog',
    templateUrl: './sms-charger-delete-dialog.component.html'
})
export class SmsChargerDeleteDialogComponent {
    smsCharger: ISmsCharger;

    constructor(
        protected smsChargerService: SmsChargerService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsChargerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsChargerListModification',
                content: 'Deleted an smsCharger'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-charger-delete-popup',
    template: ''
})
export class SmsChargerDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsCharger }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsChargerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smsCharger = smsCharger;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-charger', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-charger', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
