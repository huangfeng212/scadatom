/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmmBondComponent } from 'app/entities/smm-bond/smm-bond.component';
import { SmmBondService } from 'app/entities/smm-bond/smm-bond.service';
import { SmmBond } from 'app/shared/model/smm-bond.model';

describe('Component Tests', () => {
    describe('SmmBond Management Component', () => {
        let comp: SmmBondComponent;
        let fixture: ComponentFixture<SmmBondComponent>;
        let service: SmmBondService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmBondComponent],
                providers: []
            })
                .overrideTemplate(SmmBondComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmBondComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmBond(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmBonds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
