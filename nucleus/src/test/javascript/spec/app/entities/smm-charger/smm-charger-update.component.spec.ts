/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmChargerUpdateComponent } from 'app/entities/smm-charger/smm-charger-update.component';
import { SmmChargerService } from 'app/entities/smm-charger/smm-charger.service';
import { SmmCharger } from 'app/shared/model/smm-charger.model';

describe('Component Tests', () => {
    describe('SmmCharger Management Update Component', () => {
        let comp: SmmChargerUpdateComponent;
        let fixture: ComponentFixture<SmmChargerUpdateComponent>;
        let service: SmmChargerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmChargerUpdateComponent]
            })
                .overrideTemplate(SmmChargerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmChargerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmCharger(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmCharger = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmCharger();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmCharger = entity;
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
