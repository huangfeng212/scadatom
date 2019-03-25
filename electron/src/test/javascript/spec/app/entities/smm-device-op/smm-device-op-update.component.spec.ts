/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmDeviceOpUpdateComponent } from 'app/entities/smm-device-op/smm-device-op-update.component';
import { SmmDeviceOpService } from 'app/entities/smm-device-op/smm-device-op.service';
import { SmmDeviceOp } from 'app/shared/model/smm-device-op.model';

describe('Component Tests', () => {
    describe('SmmDeviceOp Management Update Component', () => {
        let comp: SmmDeviceOpUpdateComponent;
        let fixture: ComponentFixture<SmmDeviceOpUpdateComponent>;
        let service: SmmDeviceOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmDeviceOpUpdateComponent]
            })
                .overrideTemplate(SmmDeviceOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmDeviceOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmDeviceOpService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmDeviceOp(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmDeviceOp = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmDeviceOp();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmDeviceOp = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
