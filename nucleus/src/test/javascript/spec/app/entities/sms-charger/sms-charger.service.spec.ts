/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { SmsChargerService } from 'app/entities/sms-charger/sms-charger.service';
import { ISmsCharger, SmsCharger, Parity, Stopbit } from 'app/shared/model/sms-charger.model';

describe('Service Tests', () => {
    describe('SmsCharger Service', () => {
        let injector: TestBed;
        let service: SmsChargerService;
        let httpMock: HttpTestingController;
        let elemDefault: ISmsCharger;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SmsChargerService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new SmsCharger(0, false, 'AAAAAAA', 0, 0, Parity.None, Stopbit.NA, 0);
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

            it('should create a SmsCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new SmsCharger(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a SmsCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        port: 'BBBBBB',
                        baud: 1,
                        databit: 1,
                        parity: 'BBBBBB',
                        stopbit: 'BBBBBB',
                        respDelay: 1
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

            it('should return a list of SmsCharger', async () => {
                const returnedFromService = Object.assign(
                    {
                        enabled: true,
                        port: 'BBBBBB',
                        baud: 1,
                        databit: 1,
                        parity: 'BBBBBB',
                        stopbit: 'BBBBBB',
                        respDelay: 1
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

            it('should delete a SmsCharger', async () => {
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
