/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmsDeviceOpComponent } from 'app/entities/sms-device-op/sms-device-op.component';
import { SmsDeviceOpService } from 'app/entities/sms-device-op/sms-device-op.service';
import { SmsDeviceOp } from 'app/shared/model/sms-device-op.model';

describe('Component Tests', () => {
    describe('SmsDeviceOp Management Component', () => {
        let comp: SmsDeviceOpComponent;
        let fixture: ComponentFixture<SmsDeviceOpComponent>;
        let service: SmsDeviceOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsDeviceOpComponent],
                providers: []
            })
                .overrideTemplate(SmsDeviceOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsDeviceOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsDeviceOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsDeviceOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsDeviceOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
