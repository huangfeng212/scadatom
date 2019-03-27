/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { ParticleOpUpdateComponent } from 'app/entities/particle-op/particle-op-update.component';
import { ParticleOpService } from 'app/entities/particle-op/particle-op.service';
import { ParticleOp } from 'app/shared/model/particle-op.model';

describe('Component Tests', () => {
    describe('ParticleOp Management Update Component', () => {
        let comp: ParticleOpUpdateComponent;
        let fixture: ComponentFixture<ParticleOpUpdateComponent>;
        let service: ParticleOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ParticleOpUpdateComponent]
            })
                .overrideTemplate(ParticleOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParticleOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticleOpService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ParticleOp(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.particleOp = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ParticleOp();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.particleOp = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
