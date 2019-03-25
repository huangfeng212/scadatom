import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';
import { SmmDeviceOpService } from './smm-device-op.service';

@Component({
    selector: 'jhi-smm-device-op-delete-dialog',
    templateUrl: './smm-device-op-delete-dialog.component.html'
})
export class SmmDeviceOpDeleteDialogComponent {
    smmDeviceOp: ISmmDeviceOp;

    constructor(
        protected smmDeviceOpService: SmmDeviceOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmDeviceOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmDeviceOpListModification',
                content: 'Deleted an smmDeviceOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-device-op-delete-popup',
    template: ''
})
export class SmmDeviceOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmDeviceOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmDeviceOpDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.smmDeviceOp = smmDeviceOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-device-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-device-op', { outlets: { popup: null } }]);
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
