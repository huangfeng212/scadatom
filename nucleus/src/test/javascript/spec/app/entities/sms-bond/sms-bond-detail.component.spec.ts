/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NucleusTestModule } from '../../../test.module';
import { SmsBondDetailComponent } from 'app/entities/sms-bond/sms-bond-detail.component';
import { SmsBond } from 'app/shared/model/sms-bond.model';

describe('Component Tests', () => {
    describe('SmsBond Management Detail Component', () => {
        let comp: SmsBondDetailComponent;
        let fixture: ComponentFixture<SmsBondDetailComponent>;
        const route = ({ data: of({ smsBond: new SmsBond(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NucleusTestModule],
                declarations: [SmsBondDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsBondDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsBondDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsBond).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
