import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsChargerOp } from 'app/shared/model/sms-charger-op.model';

type EntityResponseType = HttpResponse<ISmsChargerOp>;
type EntityArrayResponseType = HttpResponse<ISmsChargerOp[]>;

@Injectable({ providedIn: 'root' })
export class SmsChargerOpService {
    public resourceUrl = SERVER_API_URL + 'api/sms-charger-ops';

    constructor(protected http: HttpClient) {}

    create(smsChargerOp: ISmsChargerOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsChargerOp);
        return this.http
            .post<ISmsChargerOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smsChargerOp: ISmsChargerOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsChargerOp);
        return this.http
            .put<ISmsChargerOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmsChargerOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmsChargerOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smsChargerOp: ISmsChargerOp): ISmsChargerOp {
        const copy: ISmsChargerOp = Object.assign({}, smsChargerOp, {
            dt: smsChargerOp.dt != null && smsChargerOp.dt.isValid() ? smsChargerOp.dt.toJSON() : null
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
            res.body.forEach((smsChargerOp: ISmsChargerOp) => {
                smsChargerOp.dt = smsChargerOp.dt != null ? moment(smsChargerOp.dt) : null;
            });
        }
        return res;
    }
}
