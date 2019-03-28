/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { ElectronUpdateComponent } from 'app/entities/electron/electron-update.component';
import { ElectronService } from 'app/entities/electron/electron.service';
import { Electron } from 'app/shared/model/electron.model';

describe('Component Tests', () => {
    describe('Electron Management Update Component', () => {
        let comp: ElectronUpdateComponent;
        let fixture: ComponentFixture<ElectronUpdateComponent>;
        let service: ElectronService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ElectronUpdateComponent]
            })
                .overrideTemplate(ElectronUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ElectronUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Electron(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.electron = entity;
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
                    const entity = new Electron();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.electron = entity;
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
