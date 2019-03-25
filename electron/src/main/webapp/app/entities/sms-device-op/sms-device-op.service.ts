import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsDeviceOp } from 'app/shared/model/sms-device-op.model';

type EntityResponseType = HttpResponse<ISmsDeviceOp>;
type EntityArrayResponseType = HttpResponse<ISmsDeviceOp[]>;

@Injectable({ providedIn: 'root' })
export class SmsDeviceOpService {
    public resourceUrl = SERVER_API_URL + 'api/sms-device-ops';

    constructor(protected http: HttpClient) {}

    create(smsDeviceOp: ISmsDeviceOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsDeviceOp);
        return this.http
            .post<ISmsDeviceOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smsDeviceOp: ISmsDeviceOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsDeviceOp);
        return this.http
            .put<ISmsDeviceOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmsDeviceOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmsDeviceOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smsDeviceOp: ISmsDeviceOp): ISmsDeviceOp {
        const copy: ISmsDeviceOp = Object.assign({}, smsDeviceOp, {
            dt: smsDeviceOp.dt != null && smsDeviceOp.dt.isValid() ? smsDeviceOp.dt.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dt = res.body.dt != null ? moment(res.body.dt) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((smsDeviceOp: ISmsDeviceOp) => {
                smsDeviceOp.dt = smsDeviceOp.dt != null ? moment(smsDeviceOp.dt) : null;
            });
        }
        return res;
    }
}
