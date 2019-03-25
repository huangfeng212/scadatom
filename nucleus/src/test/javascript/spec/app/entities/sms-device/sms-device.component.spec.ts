/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmsDeviceComponent } from 'app/entities/sms-device/sms-device.component';
import { SmsDeviceService } from 'app/entities/sms-device/sms-device.service';
import { SmsDevice } from 'app/shared/model/sms-device.model';

describe('Component Tests', () => {
    describe('SmsDevice Management Component', () => {
        let comp: SmsDeviceComponent;
        let fixture: ComponentFixture<SmsDeviceComponent>;
        let service: SmsDeviceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsDeviceComponent],
                providers: []
            })
                .overrideTemplate(SmsDeviceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsDeviceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsDevice(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsDevices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
