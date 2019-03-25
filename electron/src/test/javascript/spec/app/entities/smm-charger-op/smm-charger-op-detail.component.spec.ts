/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmChargerOpDetailComponent } from 'app/entities/smm-charger-op/smm-charger-op-detail.component';
import { SmmChargerOp } from 'app/shared/model/smm-charger-op.model';

describe('Component Tests', () => {
    describe('SmmChargerOp Management Detail Component', () => {
        let comp: SmmChargerOpDetailComponent;
        let fixture: ComponentFixture<SmmChargerOpDetailComponent>;
        const route = ({ data: of({ smmChargerOp: new SmmChargerOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmChargerOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmChargerOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmChargerOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmChargerOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
