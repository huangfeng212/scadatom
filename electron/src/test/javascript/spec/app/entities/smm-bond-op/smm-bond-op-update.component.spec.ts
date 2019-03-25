/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmBondOpUpdateComponent } from 'app/entities/smm-bond-op/smm-bond-op-update.component';
import { SmmBondOpService } from 'app/entities/smm-bond-op/smm-bond-op.service';
import { SmmBondOp } from 'app/shared/model/smm-bond-op.model';

describe('Component Tests', () => {
    describe('SmmBondOp Management Update Component', () => {
        let comp: SmmBondOpUpdateComponent;
        let fixture: ComponentFixture<SmmBondOpUpdateComponent>;
        let service: SmmBondOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmBondOpUpdateComponent]
            })
                .overrideTemplate(SmmBondOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmBondOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondOpService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmBondOp(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmBondOp = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SmmBondOp();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.smmBondOp = entity;
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
