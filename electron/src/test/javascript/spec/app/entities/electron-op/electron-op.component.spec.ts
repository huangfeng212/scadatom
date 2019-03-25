/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { ElectronOpComponent } from 'app/entities/electron-op/electron-op.component';
import { ElectronOpService } from 'app/entities/electron-op/electron-op.service';
import { ElectronOp } from 'app/shared/model/electron-op.model';

describe('Component Tests', () => {
    describe('ElectronOp Management Component', () => {
        let comp: ElectronOpComponent;
        let fixture: ComponentFixture<ElectronOpComponent>;
        let service: ElectronOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ElectronOpComponent],
                providers: []
            })
                .overrideTemplate(ElectronOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ElectronOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ElectronOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ElectronOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.electronOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
