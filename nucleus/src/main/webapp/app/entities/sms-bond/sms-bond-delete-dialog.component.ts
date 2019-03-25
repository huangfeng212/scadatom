import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISmsBond } from 'app/shared/model/sms-bond.model';
import { SmsBondService } from './sms-bond.service';

@Component({
    selector: 'jhi-sms-bond-delete-dialog',
    templateUrl: './sms-bond-delete-dialog.component.html'
})
export class SmsBondDeleteDialogComponent {
    smsBond: ISmsBond;

    constructor(protected smsBondService: SmsBondService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.smsBondService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'smsBondListModification',
                content: 'Deleted an smsBond'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sms-bond-delete-popup',
    template: ''
})
export class SmsBondDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ smsBond }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SmsBondDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.smsBond = smsBond;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/sms-bond', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/sms-bond', { outlets: { popup: null } }]);
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
