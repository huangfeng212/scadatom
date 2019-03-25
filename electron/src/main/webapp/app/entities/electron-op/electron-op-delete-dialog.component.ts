import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IElectronOp } from 'app/shared/model/electron-op.model';
import { ElectronOpService } from './electron-op.service';

@Component({
    selector: 'jhi-electron-op-delete-dialog',
    templateUrl: './electron-op-delete-dialog.component.html'
})
export class ElectronOpDeleteDialogComponent {
    electronOp: IElectronOp;

    constructor(
        protected electronOpService: ElectronOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.electronOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'electronOpListModification',
                content: 'Deleted an electronOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-electron-op-delete-popup',
    template: ''
})
export class ElectronOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ electronOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ElectronOpDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.electronOp = electronOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/electron-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/electron-op', { outlets: { popup: null } }]);
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
