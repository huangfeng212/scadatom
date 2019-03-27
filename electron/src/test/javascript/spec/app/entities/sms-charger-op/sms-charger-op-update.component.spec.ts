/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsChargerOpUpdateComponent } from 'app/entities/sms-charger-op/sms-charger-op-update.component';
import { SmsChargerOpService } from 'app/entities/sms-charger-op/sms-charger-op.service';
import { SmsChargerOp } from 'app/shared/model/sms-charger-op.model';

describe('Component Tests', () => {
    describe('SmsChargerOp Management Update Component', () => {
        let comp: SmsChargerOpUpdateComponent;
        let fixture: ComponentFixture<SmsChargerOpUpdateComponent>;
        let service: SmsChargerOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsChargerOpUpdateComponent]
            })
                .overrideTemplate(SmsChargerOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsChargerOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerOpService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SmsChargerOp(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smsChargerOp = entity;
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
                    const entity = new SmsChargerOp();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smsChargerOp = entity;
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
