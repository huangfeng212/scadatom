/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { SmsChargerOpDeleteDialogComponent } from 'app/entities/sms-charger-op/sms-charger-op-delete-dialog.component';
import { SmsChargerOpService } from 'app/entities/sms-charger-op/sms-charger-op.service';

describe('Component Tests', () => {
    describe('SmsChargerOp Management Delete Component', () => {
        let comp: SmsChargerOpDeleteDialogComponent;
        let fixture: ComponentFixture<SmsChargerOpDeleteDialogComponent>;
        let service: SmsChargerOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsChargerOpDeleteDialogComponent]
            })
                .overrideTemplate(SmsChargerOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsChargerOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerOpService);
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
