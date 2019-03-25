/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { ElectronOpDetailComponent } from 'app/entities/electron-op/electron-op-detail.component';
import { ElectronOp } from 'app/shared/model/electron-op.model';

describe('Component Tests', () => {
    describe('ElectronOp Management Detail Component', () => {
        let comp: ElectronOpDetailComponent;
        let fixture: ComponentFixture<ElectronOpDetailComponent>;
        const route = ({ data: of({ electronOp: new ElectronOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ElectronOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ElectronOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ElectronOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.electronOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
