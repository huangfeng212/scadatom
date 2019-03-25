/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmmDeviceDetailComponent } from 'app/entities/smm-device/smm-device-detail.component';
import { SmmDevice } from 'app/shared/model/smm-device.model';

describe('Component Tests', () => {
    describe('SmmDevice Management Detail Component', () => {
        let comp: SmmDeviceDetailComponent;
        let fixture: ComponentFixture<SmmDeviceDetailComponent>;
        const route = ({ data: of({ smmDevice: new SmmDevice(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmDeviceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmmDeviceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmmDeviceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smmDevice).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
