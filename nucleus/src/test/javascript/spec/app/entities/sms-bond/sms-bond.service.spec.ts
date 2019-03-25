/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { SmsBondService } from 'app/entities/sms-bond/sms-bond.service';
import { ISmsBond, SmsBond, RegType, ValueType } from 'app/shared/model/sms-bond.model';

describe('Service Tests', () => {
    describe('SmsBond Service', () => {
        let injector: TestBed;
        let service: SmsBondService;
        let httpMock: HttpTestingController;
        let elemDefault: ISmsBond;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SmsBondService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new SmsBond(0, false, RegType.Coil, 'AAAAAAA', ValueType.Uint16, 'AAAAAAA', 'AAAAAAA');
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a SmsBond', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new SmsBond(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a SmsBond', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        regType: 'BBBBBB',
                        reg: 'BBBBBB',
                        valueType: 'BBBBBB',
                        exprIn: 'BBBBBB',
                        exprOut: 'BBBBBB'
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of SmsBond', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        regType: 'BBBBBB',
                        reg: 'BBBBBB',
                        valueType: 'BBBBBB',
                        exprIn: 'BBBBBB',
                        exprOut: 'BBBBBB'
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a SmsBond', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
