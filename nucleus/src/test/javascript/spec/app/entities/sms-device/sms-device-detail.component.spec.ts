/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsDeviceDetailComponent } from 'app/entities/sms-device/sms-device-detail.component';
import { SmsDevice } from 'app/shared/model/sms-device.model';

describe('Component Tests', () => {
    describe('SmsDevice Management Detail Component', () => {
        let comp: SmsDeviceDetailComponent;
        let fixture: ComponentFixture<SmsDeviceDetailComponent>;
        const route = ({ data: of({ smsDevice: new SmsDevice(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsDeviceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsDeviceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsDeviceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsDevice).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
