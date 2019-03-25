/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NucleusTestModule } from '../../../test.module';
import { SmmDeviceComponent } from 'app/entities/smm-device/smm-device.component';
import { SmmDeviceService } from 'app/entities/smm-device/smm-device.service';
import { SmmDevice } from 'app/shared/model/smm-device.model';

describe('Component Tests', () => {
    describe('SmmDevice Management Component', () => {
        let comp: SmmDeviceComponent;
        let fixture: ComponentFixture<SmmDeviceComponent>;
        let service: SmmDeviceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmmDeviceComponent],
                providers: []
            })
                .overrideTemplate(SmmDeviceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmDeviceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmDeviceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmDevice(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmDevices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
