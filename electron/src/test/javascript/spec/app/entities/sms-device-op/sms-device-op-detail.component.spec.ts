/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsDeviceOpDetailComponent } from 'app/entities/sms-device-op/sms-device-op-detail.component';
import { SmsDeviceOp } from 'app/shared/model/sms-device-op.model';

describe('Component Tests', () => {
    describe('SmsDeviceOp Management Detail Component', () => {
        let comp: SmsDeviceOpDetailComponent;
        let fixture: ComponentFixture<SmsDeviceOpDetailComponent>;
        const route = ({ data: of({ smsDeviceOp: new SmsDeviceOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsDeviceOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsDeviceOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsDeviceOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsDeviceOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
