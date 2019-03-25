/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmDeviceUpdateComponent } from 'app/entities/smm-device/smm-device-update.component';
import { SmmDeviceService } from 'app/entities/smm-device/smm-device.service';
import { SmmDevice } from 'app/shared/model/smm-device.model';

describe('Component Tests', () => {
    describe('SmmDevice Management Update Component', () => {
        let comp: SmmDeviceUpdateComponent;
        let fixture: ComponentFixture<SmmDeviceUpdateComponent>;
        let service: SmmDeviceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmDeviceUpdateComponent]
            })
                .overrideTemplate(SmmDeviceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmDeviceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmDeviceService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmDevice(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmDevice = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmDevice();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmDevice = entity;
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
