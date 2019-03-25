/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsChargerDetailComponent } from 'app/entities/sms-charger/sms-charger-detail.component';
import { SmsCharger } from 'app/shared/model/sms-charger.model';

describe('Component Tests', () => {
    describe('SmsCharger Management Detail Component', () => {
        let comp: SmsChargerDetailComponent;
        let fixture: ComponentFixture<SmsChargerDetailComponent>;
        const route = ({ data: of({ smsCharger: new SmsCharger(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsChargerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsChargerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsChargerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsCharger).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
