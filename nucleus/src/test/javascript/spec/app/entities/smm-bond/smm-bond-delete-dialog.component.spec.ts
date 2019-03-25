/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NucleusTestModule } from '../../../test.module';
import { SmmBondDeleteDialogComponent } from 'app/entities/smm-bond/smm-bond-delete-dialog.component';
import { SmmBondService } from 'app/entities/smm-bond/smm-bond.service';

describe('Component Tests', () => {
    describe('SmmBond Management Delete Component', () => {
        let comp: SmmBondDeleteDialogComponent;
        let fixture: ComponentFixture<SmmBondDeleteDialogComponent>;
        let service: SmmBondService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmBondDeleteDialogComponent]
            })
                .overrideTemplate(SmmBondDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmBondDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondService);
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
