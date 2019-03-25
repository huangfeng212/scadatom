import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';
import { SmmChargerOpService } from './smm-charger-op.service';

@Component({
    selector: 'jhi-smm-charger-op-delete-dialog',
    templateUrl: './smm-charger-op-delete-dialog.component.html'
})
export class SmmChargerOpDeleteDialogComponent {
    smmChargerOp: ISmmChargerOp;

    constructor(
        protected smmChargerOpService: SmmChargerOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmChargerOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmChargerOpListModification',
                content: 'Deleted an smmChargerOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-charger-op-delete-popup',
    template: ''
})
export class SmmChargerOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmChargerOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmChargerOpDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.smmChargerOp = smmChargerOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-charger-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-charger-op', { outlets: { popup: null } }]);
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
