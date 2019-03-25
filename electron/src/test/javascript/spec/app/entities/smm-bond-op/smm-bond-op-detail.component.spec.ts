/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmBondOpDetailComponent } from 'app/entities/smm-bond-op/smm-bond-op-detail.component';
import { SmmBondOp } from 'app/shared/model/smm-bond-op.model';

describe('Component Tests', () => {
    describe('SmmBondOp Management Detail Component', () => {
        let comp: SmmBondOpDetailComponent;
        let fixture: ComponentFixture<SmmBondOpDetailComponent>;
        const route = ({ data: of({ smmBondOp: new SmmBondOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmBondOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmBondOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmBondOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmBondOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
