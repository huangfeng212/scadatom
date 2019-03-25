/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { ElectronComponent } from 'app/entities/electron/electron.component';
import { ElectronService } from 'app/entities/electron/electron.service';
import { Electron } from 'app/shared/model/electron.model';

describe('Component Tests', () => {
    describe('Electron Management Component', () => {
        let comp: ElectronComponent;
        let fixture: ComponentFixture<ElectronComponent>;
        let service: ElectronService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ElectronComponent],
                providers: []
            })
                .overrideTemplate(ElectronComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ElectronComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Electron(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.electrons[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
