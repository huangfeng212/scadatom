/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsBondUpdateComponent } from 'app/entities/sms-bond/sms-bond-update.component';
import { SmsBondService } from 'app/entities/sms-bond/sms-bond.service';
import { SmsBond } from 'app/shared/model/sms-bond.model';

describe('Component Tests', () => {
    describe('SmsBond Management Update Component', () => {
        let comp: SmsBondUpdateComponent;
        let fixture: ComponentFixture<SmsBondUpdateComponent>;
        let service: SmsBondService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsBondUpdateComponent]
            })
                .overrideTemplate(SmsBondUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsBondUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsBondService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsBond(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsBond = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsBond();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsBond = entity;
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
