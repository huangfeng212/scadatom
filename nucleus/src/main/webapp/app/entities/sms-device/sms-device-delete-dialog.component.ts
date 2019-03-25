import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsDevice } from 'app/shared/model/sms-device.model';
import { SmsDeviceService } from './sms-device.service';

@Component({
    selector: 'jhi-sms-device-delete-dialog',
    templateUrl: './sms-device-delete-dialog.component.html'
})
export class SmsDeviceDeleteDialogComponent {
    smsDevice: ISmsDevice;

    constructor(
        protected smsDeviceService: SmsDeviceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsDeviceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsDeviceListModification',
                content: 'Deleted an smsDevice'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-device-delete-popup',
    template: ''
})
export class SmsDeviceDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsDevice }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsDeviceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smsDevice = smsDevice;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-device', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-device', { outlets: { popup: null } }]);
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
