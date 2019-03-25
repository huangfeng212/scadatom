import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmDevice } from 'app/shared/model/smm-device.model';
import { SmmDeviceService } from './smm-device.service';

@Component({
    selector: 'jhi-smm-device-delete-dialog',
    templateUrl: './smm-device-delete-dialog.component.html'
})
export class SmmDeviceDeleteDialogComponent {
    smmDevice: ISmmDevice;

    constructor(
        protected smmDeviceService: SmmDeviceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmDeviceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmDeviceListModification',
                content: 'Deleted an smmDevice'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-device-delete-popup',
    template: ''
})
export class SmmDeviceDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmDevice }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmDeviceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smmDevice = smmDevice;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-device', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-device', { outlets: { popup: null } }]);
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
