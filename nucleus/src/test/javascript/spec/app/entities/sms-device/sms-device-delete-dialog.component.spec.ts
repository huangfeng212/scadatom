/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NucleusTestModule } from '../../../test.module';
import { SmsDeviceDeleteDialogComponent } from 'app/entities/sms-device/sms-device-delete-dialog.component';
import { SmsDeviceService } from 'app/entities/sms-device/sms-device.service';

describe('Component Tests', () => {
    describe('SmsDevice Management Delete Component', () => {
        let comp: SmsDeviceDeleteDialogComponent;
        let fixture: ComponentFixture<SmsDeviceDeleteDialogComponent>;
        let service: SmsDeviceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsDeviceDeleteDialogComponent]
            })
                .overrideTemplate(SmsDeviceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsDeviceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceService);
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
