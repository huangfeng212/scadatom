/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { SmmBondOpComponent } from 'app/entities/smm-bond-op/smm-bond-op.component';
import { SmmBondOpService } from 'app/entities/smm-bond-op/smm-bond-op.service';
import { SmmBondOp } from 'app/shared/model/smm-bond-op.model';

describe('Component Tests', () => {
    describe('SmmBondOp Management Component', () => {
        let comp: SmmBondOpComponent;
        let fixture: ComponentFixture<SmmBondOpComponent>;
        let service: SmmBondOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmmBondOpComponent],
                providers: []
            })
                .overrideTemplate(SmmBondOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SmmBondOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SmmBondOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SmmBondOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.smmBondOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
