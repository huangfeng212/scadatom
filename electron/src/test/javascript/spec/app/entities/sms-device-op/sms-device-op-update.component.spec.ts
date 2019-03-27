/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsDeviceOpUpdateComponent } from 'app/entities/sms-device-op/sms-device-op-update.component';
import { SmsDeviceOpService } from 'app/entities/sms-device-op/sms-device-op.service';
import { SmsDeviceOp } from 'app/shared/model/sms-device-op.model';

describe('Component Tests', () => {
    describe('SmsDeviceOp Management Update Component', () => {
        let comp: SmsDeviceOpUpdateComponent;
        let fixture: ComponentFixture<SmsDeviceOpUpdateComponent>;
        let service: SmsDeviceOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsDeviceOpUpdateComponent]
            })
                .overrideTemplate(SmsDeviceOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsDeviceOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceOpService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SmsDeviceOp(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smsDeviceOp = entity;
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
                    const entity = new SmsDeviceOp();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smsDeviceOp = entity;
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
