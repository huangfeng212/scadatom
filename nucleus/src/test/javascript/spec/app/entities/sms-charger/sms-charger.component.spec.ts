/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmsChargerComponent } from 'app/entities/sms-charger/sms-charger.component';
import { SmsChargerService } from 'app/entities/sms-charger/sms-charger.service';
import { SmsCharger } from 'app/shared/model/sms-charger.model';

describe('Component Tests', () => {
    describe('SmsCharger Management Component', () => {
        let comp: SmsChargerComponent;
        let fixture: ComponentFixture<SmsChargerComponent>;
        let service: SmsChargerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsChargerComponent],
                providers: []
            })
                .overrideTemplate(SmsChargerComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsChargerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsCharger(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsChargers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
