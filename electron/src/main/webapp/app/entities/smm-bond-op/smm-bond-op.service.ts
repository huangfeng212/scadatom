import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmBondOp } from 'app/shared/model/smm-bond-op.model';

type EntityResponseType = HttpResponse<ISmmBondOp>;
type EntityArrayResponseType = HttpResponse<ISmmBondOp[]>;

@Injectable({ providedIn: 'root' })
export class SmmBondOpService {
    public resourceUrl = SERVER_API_URL + 'api/smm-bond-ops';

    constructor(protected http: HttpClient) {}

    create(smmBondOp: ISmmBondOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmBondOp);
        return this.http
            .post<ISmmBondOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smmBondOp: ISmmBondOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmBondOp);
        return this.http
            .put<ISmmBondOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmmBondOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmmBondOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smmBondOp: ISmmBondOp): ISmmBondOp {
        const copy: ISmmBondOp = Object.assign({}, smmBondOp, {
            dt: smmBondOp.dt != null && smmBondOp.dt.isValid() ? smmBondOp.dt.toJSON() : null,
            writeRequestDt:
                smmBondOp.writeRequestDt != null && smmBondOp.writeRequestDt.isValid() ? smmBondOp.writeRequestDt.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dt = res.body.dt != null ? moment(res.body.dt) : null;
            res.body.writeRequestDt = res.body.writeRequestDt != null ? moment(res.body.writeRequestDt) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((smmBondOp: ISmmBondOp) => {
                smmBondOp.dt = smmBondOp.dt != null ? moment(smmBondOp.dt) : null;
                smmBondOp.writeRequestDt = smmBondOp.writeRequestDt != null ? moment(smmBondOp.writeRequestDt) : null;
            });
        }
        return res;
    }
}
