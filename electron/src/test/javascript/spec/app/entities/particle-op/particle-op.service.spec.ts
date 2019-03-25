/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ParticleOpService } from 'app/entities/particle-op/particle-op.service';
import { IParticleOp, ParticleOp, OpState } from 'app/shared/model/particle-op.model';

describe('Service Tests', () => {
    describe('ParticleOp Service', () => {
        let injector: TestBed;
        let service: ParticleOpService;
        let httpMock: HttpTestingController;
        let elemDefault: IParticleOp;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(ParticleOpService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new ParticleOp(0, OpState.Uninitialized, currentDate, 'AAAAAAA', 'AAAAAAA', currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        dt: currentDate.format(DATE_TIME_FORMAT),
                        writtenDt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a ParticleOp', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        dt: currentDate.format(DATE_TIME_FORMAT),
                        writtenDt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dt: currentDate,
                        writtenDt: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new ParticleOp(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a ParticleOp', async () => {
                const returnedFromService = Object.assign(
                    {
                        state: 'BBBBBB',
                        dt: currentDate.format(DATE_TIME_FORMAT),
                        value: 'BBBBBB',
                        writtenBy: 'BBBBBB',
                        writtenDt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        dt: currentDate,
                        writtenDt: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of ParticleOp', async () => {
                const returnedFromService = Object.assign(
                    {
                        state: 'BBBBBB',
                        dt: currentDate.format(DATE_TIME_FORMAT),
                        value: 'BBBBBB',
                        writtenBy: 'BBBBBB',
                        writtenDt: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        dt: currentDate,
                        writtenDt: currentDate
                    },
                    returnedFromService
                );
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

            it('should delete a ParticleOp', async () => {
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
