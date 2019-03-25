import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsBondOp } from 'app/shared/model/sms-bond-op.model';

type EntityResponseType = HttpResponse<ISmsBondOp>;
type EntityArrayResponseType = HttpResponse<ISmsBondOp[]>;

@Injectable({ providedIn: 'root' })
export class SmsBondOpService {
    public resourceUrl = SERVER_API_URL + 'api/sms-bond-ops';

    constructor(protected http: HttpClient) {}

    create(smsBondOp: ISmsBondOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsBondOp);
        return this.http
            .post<ISmsBondOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smsBondOp: ISmsBondOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smsBondOp);
        return this.http
            .put<ISmsBondOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmsBondOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmsBondOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smsBondOp: ISmsBondOp): ISmsBondOp {
        const copy: ISmsBondOp = Object.assign({}, smsBondOp, {
            dt: smsBondOp.dt != null && smsBondOp.dt.isValid() ? smsBondOp.dt.toJSON() : null,
            writtenDt: smsBondOp.writtenDt != null && smsBondOp.writtenDt.isValid() ? smsBondOp.writtenDt.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dt = res.body.dt != null ? moment(res.body.dt) : null;
            res.body.writtenDt = res.body.writtenDt != null ? moment(res.body.writtenDt) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((smsBondOp: ISmsBondOp) => {
                smsBondOp.dt = smsBondOp.dt != null ? moment(smsBondOp.dt) : null;
                smsBondOp.writtenDt = smsBondOp.writtenDt != null ? moment(smsBondOp.writtenDt) : null;
            });
        }
        return res;
    }
}
