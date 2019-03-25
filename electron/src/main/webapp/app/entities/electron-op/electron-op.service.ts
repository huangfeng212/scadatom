import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IElectronOp } from 'app/shared/model/electron-op.model';

type EntityResponseType = HttpResponse<IElectronOp>;
type EntityArrayResponseType = HttpResponse<IElectronOp[]>;

@Injectable({ providedIn: 'root' })
export class ElectronOpService {
    public resourceUrl = SERVER_API_URL + 'api/electron-ops';

    constructor(protected http: HttpClient) {}

    create(electronOp: IElectronOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(electronOp);
        return this.http
            .post<IElectronOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(electronOp: IElectronOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(electronOp);
        return this.http
            .put<IElectronOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IElectronOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IElectronOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(electronOp: IElectronOp): IElectronOp {
        const copy: IElectronOp = Object.assign({}, electronOp, {
            dt: electronOp.dt != null && electronOp.dt.isValid() ? electronOp.dt.toJSON() : null
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
            res.body.forEach((electronOp: IElectronOp) => {
                electronOp.dt = electronOp.dt != null ? moment(electronOp.dt) : null;
            });
        }
        return res;
    }
}
