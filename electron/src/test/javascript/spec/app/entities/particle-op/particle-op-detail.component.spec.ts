/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { ParticleOpDetailComponent } from 'app/entities/particle-op/particle-op-detail.component';
import { ParticleOp } from 'app/shared/model/particle-op.model';

describe('Component Tests', () => {
    describe('ParticleOp Management Detail Component', () => {
        let comp: ParticleOpDetailComponent;
        let fixture: ComponentFixture<ParticleOpDetailComponent>;
        const route = ({ data: of({ particleOp: new ParticleOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ParticleOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ParticleOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParticleOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.particleOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
