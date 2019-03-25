import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmBond } from 'app/shared/model/smm-bond.model';
import { SmmBondService } from './smm-bond.service';

@Component({
    selector: 'jhi-smm-bond-delete-dialog',
    templateUrl: './smm-bond-delete-dialog.component.html'
})
export class SmmBondDeleteDialogComponent {
    smmBond: ISmmBond;

    constructor(protected smmBondService: SmmBondService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmBondService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmBondListModification',
                content: 'Deleted an smmBond'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-bond-delete-popup',
    template: ''
})
export class SmmBondDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmBond }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmBondDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smmBond = smmBond;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-bond', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-bond', { outlets: { popup: null } }]);
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
