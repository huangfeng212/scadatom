/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NucleusTestModule } from '../../../test.module';
import { SmmChargerDeleteDialogComponent } from 'app/entities/smm-charger/smm-charger-delete-dialog.component';
import { SmmChargerService } from 'app/entities/smm-charger/smm-charger.service';

describe('Component Tests', () => {
    describe('SmmCharger Management Delete Component', () => {
        let comp: SmmChargerDeleteDialogComponent;
        let fixture: ComponentFixture<SmmChargerDeleteDialogComponent>;
        let service: SmmChargerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmChargerDeleteDialogComponent]
            })
                .overrideTemplate(SmmChargerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmChargerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerService);
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
