/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { SmsBondOpDeleteDialogComponent } from 'app/entities/sms-bond-op/sms-bond-op-delete-dialog.component';
import { SmsBondOpService } from 'app/entities/sms-bond-op/sms-bond-op.service';

describe('Component Tests', () => {
    describe('SmsBondOp Management Delete Component', () => {
        let comp: SmsBondOpDeleteDialogComponent;
        let fixture: ComponentFixture<SmsBondOpDeleteDialogComponent>;
        let service: SmsBondOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsBondOpDeleteDialogComponent]
            })
                .overrideTemplate(SmsBondOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsBondOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsBondOpService);
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
