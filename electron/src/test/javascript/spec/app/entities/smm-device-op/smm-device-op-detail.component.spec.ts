/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmmDeviceOpDetailComponent } from 'app/entities/smm-device-op/smm-device-op-detail.component';
import { SmmDeviceOp } from 'app/shared/model/smm-device-op.model';

describe('Component Tests', () => {
    describe('SmmDeviceOp Management Detail Component', () => {
        let comp: SmmDeviceOpDetailComponent;
        let fixture: ComponentFixture<SmmDeviceOpDetailComponent>;
        const route = ({ data: of({ smmDeviceOp: new SmmDeviceOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmDeviceOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmDeviceOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmDeviceOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmDeviceOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
