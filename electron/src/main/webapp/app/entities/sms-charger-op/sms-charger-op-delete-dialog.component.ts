import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';
import { SmsChargerOpService } from './sms-charger-op.service';

@Component({
    selector: 'jhi-sms-charger-op-delete-dialog',
    templateUrl: './sms-charger-op-delete-dialog.component.html'
})
export class SmsChargerOpDeleteDialogComponent {
    smsChargerOp: ISmsChargerOp;

    constructor(
        protected smsChargerOpService: SmsChargerOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsChargerOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsChargerOpListModification',
                content: 'Deleted an smsChargerOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-charger-op-delete-popup',
    template: ''
})
export class SmsChargerOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsChargerOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsChargerOpDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.smsChargerOp = smsChargerOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-charger-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-charger-op', { outlets: { popup: null } }]);
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
