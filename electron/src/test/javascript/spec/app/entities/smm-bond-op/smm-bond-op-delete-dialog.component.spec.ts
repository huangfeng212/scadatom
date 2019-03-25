/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { SmmBondOpDeleteDialogComponent } from 'app/entities/smm-bond-op/smm-bond-op-delete-dialog.component';
import { SmmBondOpService } from 'app/entities/smm-bond-op/smm-bond-op.service';

describe('Component Tests', () => {
    describe('SmmBondOp Management Delete Component', () => {
        let comp: SmmBondOpDeleteDialogComponent;
        let fixture: ComponentFixture<SmmBondOpDeleteDialogComponent>;
        let service: SmmBondOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmBondOpDeleteDialogComponent]
            })
                .overrideTemplate(SmmBondOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmBondOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondOpService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
