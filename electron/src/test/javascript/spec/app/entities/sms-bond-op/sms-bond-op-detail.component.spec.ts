/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ElectronTestModule } from '../../../test.module';
import { SmsBondOpDetailComponent } from 'app/entities/sms-bond-op/sms-bond-op-detail.component';
import { SmsBondOp } from 'app/shared/model/sms-bond-op.model';

describe('Component Tests', () => {
    describe('SmsBondOp Management Detail Component', () => {
        let comp: SmsBondOpDetailComponent;
        let fixture: ComponentFixture<SmsBondOpDetailComponent>;
        const route = ({ data: of({ smsBondOp: new SmsBondOp(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ElectronTestModule],
                declarations: [SmsBondOpDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SmsBondOpDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SmsBondOpDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.smsBondOp).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
