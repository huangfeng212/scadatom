import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmmCharger } from 'app/shared/model/smm-charger.model';
import { SmmChargerService } from './smm-charger.service';

@Component({
    selector: 'jhi-smm-charger-delete-dialog',
    templateUrl: './smm-charger-delete-dialog.component.html'
})
export class SmmChargerDeleteDialogComponent {
    smmCharger: ISmmCharger;

    constructor(
        protected smmChargerService: SmmChargerService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smmChargerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smmChargerListModification',
                content: 'Deleted an smmCharger'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-smm-charger-delete-popup',
    template: ''
})
export class SmmChargerDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smmCharger }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmmChargerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smmCharger = smmCharger;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/smm-charger', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/smm-charger', { outlets: { popup: null } }]);
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
