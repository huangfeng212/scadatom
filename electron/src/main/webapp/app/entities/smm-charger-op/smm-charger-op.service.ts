import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmChargerOp } from 'app/shared/model/smm-charger-op.model';

type EntityResponseType = HttpResponse<ISmmChargerOp>;
type EntityArrayResponseType = HttpResponse<ISmmChargerOp[]>;

@Injectable({ providedIn: 'root' })
export class SmmChargerOpService {
    public resourceUrl = SERVER_API_URL + 'api/smm-charger-ops';

    constructor(protected http: HttpClient) {}

    create(smmChargerOp: ISmmChargerOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmChargerOp);
        return this.http
            .post<ISmmChargerOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smmChargerOp: ISmmChargerOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmChargerOp);
        return this.http
            .put<ISmmChargerOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmmChargerOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmmChargerOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smmChargerOp: ISmmChargerOp): ISmmChargerOp {
        const copy: ISmmChargerOp = Object.assign({}, smmChargerOp, {
            dt: smmChargerOp.dt != null && smmChargerOp.dt.isValid() ? smmChargerOp.dt.toJSON() : null
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
            res.body.forEach((smmChargerOp: ISmmChargerOp) => {
                smmChargerOp.dt = smmChargerOp.dt != null ? moment(smmChargerOp.dt) : null;
            });
        }
        return res;
    }
}
