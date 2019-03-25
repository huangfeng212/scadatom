/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { ParticleDetailComponent } from 'app/entities/particle/particle-detail.component';
import { Particle } from 'app/shared/model/particle.model';

describe('Component Tests', () => {
    describe('Particle Management Detail Component', () => {
        let comp: ParticleDetailComponent;
        let fixture: ComponentFixture<ParticleDetailComponent>;
        const route = ({ data: of({ particle: new Particle(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ParticleDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ParticleDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ParticleDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.particle).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
