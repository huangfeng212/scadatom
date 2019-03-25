/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmsBondOpComponent } from 'app/entities/sms-bond-op/sms-bond-op.component';
import { SmsBondOpService } from 'app/entities/sms-bond-op/sms-bond-op.service';
import { SmsBondOp } from 'app/shared/model/sms-bond-op.model';

describe('Component Tests', () => {
    describe('SmsBondOp Management Component', () => {
        let comp: SmsBondOpComponent;
        let fixture: ComponentFixture<SmsBondOpComponent>;
        let service: SmsBondOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsBondOpComponent],
                providers: []
            })
                .overrideTemplate(SmsBondOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsBondOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsBondOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsBondOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsBondOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
