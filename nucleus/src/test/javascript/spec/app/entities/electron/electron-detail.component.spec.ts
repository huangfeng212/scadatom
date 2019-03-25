/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { ElectronDetailComponent } from 'app/entities/electron/electron-detail.component';
import { Electron } from 'app/shared/model/electron.model';

describe('Component Tests', () => {
    describe('Electron Management Detail Component', () => {
        let comp: ElectronDetailComponent;
        let fixture: ComponentFixture<ElectronDetailComponent>;
        const route = ({ data: of({ electron: new Electron(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [ElectronDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ElectronDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ElectronDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.electron).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
