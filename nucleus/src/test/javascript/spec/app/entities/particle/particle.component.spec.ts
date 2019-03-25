/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { ParticleComponent } from 'app/entities/particle/particle.component';
import { ParticleService } from 'app/entities/particle/particle.service';
import { Particle } from 'app/shared/model/particle.model';

describe('Component Tests', () => {
    describe('Particle Management Component', () => {
        let comp: ParticleComponent;
        let fixture: ComponentFixture<ParticleComponent>;
        let service: ParticleService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ParticleComponent],
                providers: []
            })
                .overrideTemplate(ParticleComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParticleComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticleService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Particle(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.particles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
