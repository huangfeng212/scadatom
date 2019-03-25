/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsBondOpUpdateComponent } from 'app/entities/sms-bond-op/sms-bond-op-update.component';
import { SmsBondOpService } from 'app/entities/sms-bond-op/sms-bond-op.service';
import { SmsBondOp } from 'app/shared/model/sms-bond-op.model';

describe('Component Tests', () => {
    describe('SmsBondOp Management Update Component', () => {
        let comp: SmsBondOpUpdateComponent;
        let fixture: ComponentFixture<SmsBondOpUpdateComponent>;
        let service: SmsBondOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsBondOpUpdateComponent]
            })
                .overrideTemplate(SmsBondOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmsBondOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmsBondOpService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsBondOp(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsBondOp = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmsBondOp();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smsBondOp = entity;
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
