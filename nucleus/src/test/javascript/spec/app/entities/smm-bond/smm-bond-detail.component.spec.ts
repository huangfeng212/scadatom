/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmBondDetailComponent } from 'app/entities/smm-bond/smm-bond-detail.component';
import { SmmBond } from 'app/shared/model/smm-bond.model';

describe('Component Tests', () => {
    describe('SmmBond Management Detail Component', () => {
        let comp: SmmBondDetailComponent;
        let fixture: ComponentFixture<SmmBondDetailComponent>;
        const route = ({ data: of({ smmBond: new SmmBond(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmBondDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmBondDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmBondDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmBond).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
