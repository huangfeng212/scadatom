/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { SmsDeviceOpDeleteDialogComponent } from 'app/entities/sms-device-op/sms-device-op-delete-dialog.component';
import { SmsDeviceOpService } from 'app/entities/sms-device-op/sms-device-op.service';

describe('Component Tests', () => {
    describe('SmsDeviceOp Management Delete Component', () => {
        let comp: SmsDeviceOpDeleteDialogComponent;
        let fixture: ComponentFixture<SmsDeviceOpDeleteDialogComponent>;
        let service: SmsDeviceOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsDeviceOpDeleteDialogComponent]
            })
                .overrideTemplate(SmsDeviceOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsDeviceOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceOpService);
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
