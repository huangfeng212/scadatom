import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';
import { SmsDeviceOpService } from './sms-device-op.service';

@Component({
    selector: 'jhi-sms-device-op-delete-dialog',
    templateUrl: './sms-device-op-delete-dialog.component.html'
})
export class SmsDeviceOpDeleteDialogComponent {
    smsDeviceOp: ISmsDeviceOp;

    constructor(
        protected smsDeviceOpService: SmsDeviceOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsDeviceOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsDeviceOpListModification',
                content: 'Deleted an smsDeviceOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-device-op-delete-popup',
    template: ''
})
export class SmsDeviceOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsDeviceOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsDeviceOpDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.smsDeviceOp = smsDeviceOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-device-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-device-op', { outlets: { popup: null } }]);
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
