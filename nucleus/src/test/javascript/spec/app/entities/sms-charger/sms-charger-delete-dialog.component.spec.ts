/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NucleusTestModule } from '../../../test.module';
import { SmsChargerDeleteDialogComponent } from 'app/entities/sms-charger/sms-charger-delete-dialog.component';
import { SmsChargerService } from 'app/entities/sms-charger/sms-charger.service';

describe('Component Tests', () => {
    describe('SmsCharger Management Delete Component', () => {
        let comp: SmsChargerDeleteDialogComponent;
        let fixture: ComponentFixture<SmsChargerDeleteDialogComponent>;
        let service: SmsChargerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsChargerDeleteDialogComponent]
            })
                .overrideTemplate(SmsChargerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsChargerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerService);
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
