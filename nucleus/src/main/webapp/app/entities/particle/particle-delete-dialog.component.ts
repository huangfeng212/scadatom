import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParticle } from 'app/shared/model/particle.model';
import { ParticleService } from './particle.service';

@Component({
    selector: 'jhi-particle-delete-dialog',
    templateUrl: './particle-delete-dialog.component.html'
})
export class ParticleDeleteDialogComponent {
    particle: IParticle;

    constructor(protected particleService: ParticleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.particleService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'particleListModification',
                content: 'Deleted an particle'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-particle-delete-popup',
    template: ''
})
export class ParticleDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ particle }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ParticleDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.particle = particle;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/particle', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/particle', { outlets: { popup: null } }]);
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
