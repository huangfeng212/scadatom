/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmsChargerOpComponent } from 'app/entities/sms-charger-op/sms-charger-op.component';
import { SmsChargerOpService } from 'app/entities/sms-charger-op/sms-charger-op.service';
import { SmsChargerOp } from 'app/shared/model/sms-charger-op.model';

describe('Component Tests', () => {
    describe('SmsChargerOp Management Component', () => {
        let comp: SmsChargerOpComponent;
        let fixture: ComponentFixture<SmsChargerOpComponent>;
        let service: SmsChargerOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsChargerOpComponent],
                providers: []
            })
                .overrideTemplate(SmsChargerOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsChargerOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsChargerOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsChargerOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsChargerOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
