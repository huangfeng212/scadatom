import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParticleOp } from 'app/shared/model/particle-op.model';
import { ParticleOpService } from './particle-op.service';

@Component({
    selector: 'jhi-particle-op-delete-dialog',
    templateUrl: './particle-op-delete-dialog.component.html'
})
export class ParticleOpDeleteDialogComponent {
    particleOp: IParticleOp;

    constructor(
        protected particleOpService: ParticleOpService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.particleOpService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'particleOpListModification',
                content: 'Deleted an particleOp'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-particle-op-delete-popup',
    template: ''
})
export class ParticleOpDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ particleOp }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ParticleOpDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.particleOp = particleOp;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/particle-op', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/particle-op', { outlets: { popup: null } }]);
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
