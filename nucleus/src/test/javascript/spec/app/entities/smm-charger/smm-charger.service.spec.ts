/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { SmmChargerService } from 'app/entities/smm-charger/smm-charger.service';
import { ISmmCharger, SmmCharger, Parity, Stopbit } from 'app/shared/model/smm-charger.model';

describe('Service Tests', () => {
    describe('SmmCharger Service', () => {
        let injector: TestBed;
        let service: SmmChargerService;
        let httpMock: HttpTestingController;
        let elemDefault: ISmmCharger;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SmmChargerService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new SmmCharger(0, false, 'AAAAAAA', 0, 0, Parity.None, Stopbit.NA, 0, 0, 0, 0);
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

            it('should create a SmmCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new SmmCharger(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a SmmCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        port: 'BBBBBB',
                        baud: 1,
                        databit: 1,
                        parity: 'BBBBBB',
                        stopbit: 'BBBBBB',
                        timeout: 1,
                        retry: 1,
                        transDelay: 1,
                        batchDelay: 1
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

            it('should return a list of SmmCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        port: 'BBBBBB',
                        baud: 1,
                        databit: 1,
                        parity: 'BBBBBB',
                        stopbit: 'BBBBBB',
                        timeout: 1,
                        retry: 1,
                        transDelay: 1,
                        batchDelay: 1
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

            it('should delete a SmmCharger', async () => {
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
