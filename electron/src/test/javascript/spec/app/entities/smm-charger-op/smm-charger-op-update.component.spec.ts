/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmChargerOpUpdateComponent } from 'app/entities/smm-charger-op/smm-charger-op-update.component';
import { SmmChargerOpService } from 'app/entities/smm-charger-op/smm-charger-op.service';
import { SmmChargerOp } from 'app/shared/model/smm-charger-op.model';

describe('Component Tests', () => {
    describe('SmmChargerOp Management Update Component', () => {
        let comp: SmmChargerOpUpdateComponent;
        let fixture: ComponentFixture<SmmChargerOpUpdateComponent>;
        let service: SmmChargerOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmChargerOpUpdateComponent]
            })
                .overrideTemplate(SmmChargerOpUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmChargerOpUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerOpService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SmmChargerOp(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smmChargerOp = entity;
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
                    const entity = new SmmChargerOp();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.smmChargerOp = entity;
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
