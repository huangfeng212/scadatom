import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';
import { SmmBondOpService } from './smm-bond-op.service';

@Component({
    selector: 'jhi-smm-bond-op-delete-dialog',
    templateUrl: './smm-bond-op-delete-dialog.component.html'
})
export class SmmBondOpDeleteDialogComponent {
    smmBondOp: ISmmBondOp;

    constructor(
        protected smmBondOpService: SmmBondOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmBondOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmBondOpListModification',
                content: 'Deleted an smmBondOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-bond-op-delete-popup',
    template: ''
})
export class SmmBondOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmBondOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmBondOpDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smmBondOp = smmBondOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-bond-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-bond-op', { outlets: { popup: null } }]);
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
