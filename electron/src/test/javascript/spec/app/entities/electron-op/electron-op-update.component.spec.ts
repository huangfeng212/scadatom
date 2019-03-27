/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { ElectronOpUpdateComponent } from 'app/entities/electron-op/electron-op-update.component';
import { ElectronOpService } from 'app/entities/electron-op/electron-op.service';
import { ElectronOp } from 'app/shared/model/electron-op.model';

describe('Component Tests', () => {
    describe('ElectronOp Management Update Component', () => {
        let comp: ElectronOpUpdateComponent;
        let fixture: ComponentFixture<ElectronOpUpdateComponent>;
        let service: ElectronOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ElectronOpUpdateComponent]
            })
                .overrideTemplate(ElectronOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ElectronOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronOpService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ElectronOp(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.electronOp = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ElectronOp();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.electronOp = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
