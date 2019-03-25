import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';
import { SmsBondOpService } from './sms-bond-op.service';

@Component({
    selector: 'jhi-sms-bond-op-delete-dialog',
    templateUrl: './sms-bond-op-delete-dialog.component.html'
})
export class SmsBondOpDeleteDialogComponent {
    smsBondOp: ISmsBondOp;

    constructor(
        protected smsBondOpService: SmsBondOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsBondOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsBondOpListModification',
                content: 'Deleted an smsBondOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-bond-op-delete-popup',
    template: ''
})
export class SmsBondOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsBondOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsBondOpDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smsBondOp = smsBondOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-bond-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-bond-op', { outlets: { popup: null } }]);
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
