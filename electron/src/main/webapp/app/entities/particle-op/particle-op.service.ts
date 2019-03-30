import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IParticleOp } from 'app/shared/model/particle-op.model';
import { IOpCtrlReq } from 'app/shared/model/operation.model';

type EntityResponseType = HttpResponse<IParticleOp>;
type EntityArrayResponseType = HttpResponse<IParticleOp[]>;

@Injectable({ providedIn: 'root' })
export class ParticleOpService {
    public resourceUrl = SERVER_API_URL + 'api/particle-ops';
    public operationUrl = SERVER_API_URL + 'api/operation/particle';

    constructor(protected http: HttpClient) {}

    create(particleOp: IParticleOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(particleOp);
        return this.http
            .post<IParticleOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(particleOp: IParticleOp): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(particleOp);
        return this.http
            .put<IParticleOp>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IParticleOp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IParticleOp[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    ctrl(opCtrlReq: IOpCtrlReq): Observable<HttpResponse<any>> {
        return this.http.put<IOpCtrlReq>(this.operationUrl, opCtrlReq, { observe: 'response' });
    }

    protected convertDateFromClient(particleOp: IParticleOp): IParticleOp {
        const copy: IParticleOp = Object.assign({}, particleOp, {
            dt: particleOp.dt != null && particleOp.dt.isValid() ? particleOp.dt.toJSON() : null,
            writtenDt: particleOp.writtenDt != null && particleOp.writtenDt.isValid() ? particleOp.writtenDt.toJSON() : null
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
            res.body.forEach((particleOp: IParticleOp) => {
                particleOp.dt = particleOp.dt != null ? moment(particleOp.dt) : null;
                particleOp.writtenDt = particleOp.writtenDt != null ? moment(particleOp.writtenDt) : null;
            });
        }
        return res;
    }
}
