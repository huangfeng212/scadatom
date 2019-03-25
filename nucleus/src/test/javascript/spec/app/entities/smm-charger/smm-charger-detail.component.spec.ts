/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmChargerDetailComponent } from 'app/entities/smm-charger/smm-charger-detail.component';
import { SmmCharger } from 'app/shared/model/smm-charger.model';

describe('Component Tests', () => {
    describe('SmmCharger Management Detail Component', () => {
        let comp: SmmChargerDetailComponent;
        let fixture: ComponentFixture<SmmChargerDetailComponent>;
        const route = ({ data: of({ smmCharger: new SmmCharger(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmChargerDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmChargerDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmChargerDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmCharger).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
