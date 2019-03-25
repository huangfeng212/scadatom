/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { ParticleUpdateComponent } from 'app/entities/particle/particle-update.component';
import { ParticleService } from 'app/entities/particle/particle.service';
import { Particle } from 'app/shared/model/particle.model';

describe('Component Tests', () => {
    describe('Particle Management Update Component', () => {
        let comp: ParticleUpdateComponent;
        let fixture: ComponentFixture<ParticleUpdateComponent>;
        let service: ParticleService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ParticleUpdateComponent]
            })
                .overrideTemplate(ParticleUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParticleUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticleService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Particle(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.particle = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Particle();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.particle = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
