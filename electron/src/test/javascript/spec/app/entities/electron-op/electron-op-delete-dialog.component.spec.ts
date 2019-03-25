/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ElectronTestModule } from '../../../test.module';
import { ElectronOpDeleteDialogComponent } from 'app/entities/electron-op/electron-op-delete-dialog.component';
import { ElectronOpService } from 'app/entities/electron-op/electron-op.service';

describe('Component Tests', () => {
    describe('ElectronOp Management Delete Component', () => {
        let comp: ElectronOpDeleteDialogComponent;
        let fixture: ComponentFixture<ElectronOpDeleteDialogComponent>;
        let service: ElectronOpService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ElectronOpDeleteDialogComponent]
            })
                .overrideTemplate(ElectronOpDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ElectronOpDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronOpService);
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
