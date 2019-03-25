/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ElectronTestModule } from '../../../test.module';
import { ParticleOpComponent } from 'app/entities/particle-op/particle-op.component';
import { ParticleOpService } from 'app/entities/particle-op/particle-op.service';
import { ParticleOp } from 'app/shared/model/particle-op.model';

describe('Component Tests', () => {
    describe('ParticleOp Management Component', () => {
        let comp: ParticleOpComponent;
        let fixture: ComponentFixture<ParticleOpComponent>;
        let service: ParticleOpService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [ParticleOpComponent],
                providers: []
            })
                .overrideTemplate(ParticleOpComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ParticleOpComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParticleOpService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new ParticleOp(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.particleOps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
