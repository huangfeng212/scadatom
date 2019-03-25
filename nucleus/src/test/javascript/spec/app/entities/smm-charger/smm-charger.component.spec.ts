/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmmChargerComponent } from 'app/entities/smm-charger/smm-charger.component';
import { SmmChargerService } from 'app/entities/smm-charger/smm-charger.service';
import { SmmCharger } from 'app/shared/model/smm-charger.model';

describe('Component Tests', () => {
    describe('SmmCharger Management Component', () => {
        let comp: SmmChargerComponent;
        let fixture: ComponentFixture<SmmChargerComponent>;
        let service: SmmChargerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmChargerComponent],
                providers: []
            })
                .overrideTemplate(SmmChargerComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmChargerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmCharger(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmChargers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
