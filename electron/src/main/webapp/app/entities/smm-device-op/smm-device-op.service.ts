import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmDeviceOp } from 'app/shared/model/smm-device-op.model';

type EntityResponseType = HttpResponse<ISmmDeviceOp>;
type EntityArrayResponseType = HttpResponse<ISmmDeviceOp[]>;

@Injectable({ providedIn: 'root' })
export class SmmDeviceOpService {
    public resourceUrl = SERVER_API_URL + 'api/smm-device-ops';

    constructor(protected http: HttpClient) {}

    create(smmDeviceOp: ISmmDeviceOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmDeviceOp);
        return this.http
            .post<ISmmDeviceOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(smmDeviceOp: ISmmDeviceOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(smmDeviceOp);
        return this.http
            .put<ISmmDeviceOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISmmDeviceOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISmmDeviceOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(smmDeviceOp: ISmmDeviceOp): ISmmDeviceOp {
        const copy: ISmmDeviceOp = Object.assign({}, smmDeviceOp, {
            dt: smmDeviceOp.dt != null && smmDeviceOp.dt.isValid() ? smmDeviceOp.dt.toJSON() : null
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
            res.body.forEach((smmDeviceOp: ISmmDeviceOp) => {
                smmDeviceOp.dt = smmDeviceOp.dt != null ? moment(smmDeviceOp.dt) : null;
            });
        }
        return res;
    }
}
