/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmmDeviceOpComponent } from 'app/entities/smm-device-op/smm-device-op.component';
import { SmmDeviceOpService } from 'app/entities/smm-device-op/smm-device-op.service';
import { SmmDeviceOp } from 'app/shared/model/smm-device-op.model';

describe('Component Tests', () => {
    describe('SmmDeviceOp Management Component', () => {
        let comp: SmmDeviceOpComponent;
        let fixture: ComponentFixture<SmmDeviceOpComponent>;
        let service: SmmDeviceOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmDeviceOpComponent],
                providers: []
            })
                .overrideTemplate(SmmDeviceOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmDeviceOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmDeviceOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmDeviceOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmDeviceOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
