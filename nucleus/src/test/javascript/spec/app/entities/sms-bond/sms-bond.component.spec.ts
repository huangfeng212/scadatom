/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmsBondComponent } from 'app/entities/sms-bond/sms-bond.component';
import { SmsBondService } from 'app/entities/sms-bond/sms-bond.service';
import { SmsBond } from 'app/shared/model/sms-bond.model';

describe('Component Tests', () => {
    describe('SmsBond Management Component', () => {
        let comp: SmsBondComponent;
        let fixture: ComponentFixture<SmsBondComponent>;
        let service: SmsBondService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsBondComponent],
                providers: []
            })
                .overrideTemplate(SmsBondComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsBondComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsBondService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmsBond(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smsBonds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
