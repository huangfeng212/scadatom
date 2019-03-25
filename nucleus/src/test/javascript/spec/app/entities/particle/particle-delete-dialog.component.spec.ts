/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NucleusTestModule } from '../../../test.module';
import { ParticleDeleteDialogComponent } from 'app/entities/particle/particle-delete-dialog.component';
import { ParticleService } from 'app/entities/particle/particle.service';

describe('Component Tests', () => {
    describe('Particle Management Delete Component', () => {
        let comp: ParticleDeleteDialogComponent;
        let fixture: ComponentFixture<ParticleDeleteDialogComponent>;
        let service: ParticleService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ParticleDeleteDialogComponent]
            })
                .overrideTemplate(ParticleDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParticleDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticleService);
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
