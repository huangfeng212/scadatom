/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsChargerOpDetailComponent } from 'app/entities/sms-charger-op/sms-charger-op-detail.component';
import { SmsChargerOp } from 'app/shared/model/sms-charger-op.model';

describe('Component Tests', () => {
    describe('SmsChargerOp Management Detail Component', () => {
        let comp: SmsChargerOpDetailComponent;
        let fixture: ComponentFixture<SmsChargerOpDetailComponent>;
        const route = ({ data: of({ smsChargerOp: new SmsChargerOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsChargerOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsChargerOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsChargerOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsChargerOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
