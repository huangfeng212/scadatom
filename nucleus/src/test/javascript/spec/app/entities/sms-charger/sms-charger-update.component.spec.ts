/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsChargerUpdateComponent } from 'app/entities/sms-charger/sms-charger-update.component';
import { SmsChargerService } from 'app/entities/sms-charger/sms-charger.service';
import { SmsCharger } from 'app/shared/model/sms-charger.model';

describe('Component Tests', () => {
    describe('SmsCharger Management Update Component', () => {
        let comp: SmsChargerUpdateComponent;
        let fixture: ComponentFixture<SmsChargerUpdateComponent>;
        let service: SmsChargerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsChargerUpdateComponent]
            })
                .overrideTemplate(SmsChargerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsChargerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsCharger(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsCharger = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsCharger();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsCharger = entity;
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
