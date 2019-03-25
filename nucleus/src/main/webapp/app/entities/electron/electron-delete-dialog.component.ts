import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IElectron } from 'app/shared/model/electron.model';
import { ElectronService } from './electron.service';

@Component({
    selector: 'jhi-electron-delete-dialog',
    templateUrl: './electron-delete-dialog.component.html'
})
export class ElectronDeleteDialogComponent {
    electron: IElectron;

    constructor(protected electronService: ElectronService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.electronService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'electronListModification',
                content: 'Deleted an electron'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-electron-delete-popup',
    template: ''
})
export class ElectronDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ electron }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ElectronDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.electron = electron;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/electron', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/electron', { outlets: { popup: null } }]);
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
