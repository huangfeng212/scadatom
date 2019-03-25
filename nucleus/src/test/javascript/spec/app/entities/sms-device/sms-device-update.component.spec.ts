/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsDeviceUpdateComponent } from 'app/entities/sms-device/sms-device-update.component';
import { SmsDeviceService } from 'app/entities/sms-device/sms-device.service';
import { SmsDevice } from 'app/shared/model/sms-device.model';

describe('Component Tests', () => {
    describe('SmsDevice Management Update Component', () => {
        let comp: SmsDeviceUpdateComponent;
        let fixture: ComponentFixture<SmsDeviceUpdateComponent>;
        let service: SmsDeviceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsDeviceUpdateComponent]
            })
                .overrideTemplate(SmsDeviceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsDeviceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsDevice(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsDevice = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsDevice();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsDevice = entity;
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
