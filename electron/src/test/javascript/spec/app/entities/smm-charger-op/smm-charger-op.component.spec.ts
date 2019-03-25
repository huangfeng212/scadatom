/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmmChargerOpComponent } from 'app/entities/smm-charger-op/smm-charger-op.component';
import { SmmChargerOpService } from 'app/entities/smm-charger-op/smm-charger-op.service';
import { SmmChargerOp } from 'app/shared/model/smm-charger-op.model';

describe('Component Tests', () => {
    describe('SmmChargerOp Management Component', () => {
        let comp: SmmChargerOpComponent;
        let fixture: ComponentFixture<SmmChargerOpComponent>;
        let service: SmmChargerOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmChargerOpComponent],
                providers: []
            })
                .overrideTemplate(SmmChargerOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmChargerOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmChargerOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmChargerOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmChargerOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
