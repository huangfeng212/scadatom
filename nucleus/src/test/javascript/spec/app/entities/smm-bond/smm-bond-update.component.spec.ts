/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmBondUpdateComponent } from 'app/entities/smm-bond/smm-bond-update.component';
import { SmmBondService } from 'app/entities/smm-bond/smm-bond.service';
import { SmmBond } from 'app/shared/model/smm-bond.model';

describe('Component Tests', () => {
    describe('SmmBond Management Update Component', () => {
        let comp: SmmBondUpdateComponent;
        let fixture: ComponentFixture<SmmBondUpdateComponent>;
        let service: SmmBondService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmBondUpdateComponent]
            })
                .overrideTemplate(SmmBondUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmBondUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmBond(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmBond = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmBond();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmBond = entity;
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
