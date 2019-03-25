/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { SmmChargerOpDeleteDialogComponent } from 'app/entities/smm-charger-op/smm-charger-op-delete-dialog.component';
import { SmmChargerOpService } from 'app/entities/smm-charger-op/smm-charger-op.service';

describe('Component Tests', () => {
    describe('SmmChargerOp Management Delete Component', () => {
        let comp: SmmChargerOpDeleteDialogComponent;
        let fixture: ComponentFixture<SmmChargerOpDeleteDialogComponent>;
        let service: SmmChargerOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmChargerOpDeleteDialogComponent]
            })
                .overrideTemplate(SmmChargerOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmChargerOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerOpService);
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
