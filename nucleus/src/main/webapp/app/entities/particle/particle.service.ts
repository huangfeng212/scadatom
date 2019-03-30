import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IParticle} from 'app/shared/model/particle.model';
import {IOpCtrlReq} from 'app/shared/model/operation.model';
import {IParticleOp} from 'app/shared/model/particle-op.model';

type EntityResponseType = HttpResponse<IParticle>;
type EntityArrayResponseType = HttpResponse<IParticle[]>;
type EntityOpResponseType = HttpResponse<IParticleOp>;

@Injectable({providedIn: 'root'})
export class ParticleService {
  public resourceUrl = SERVER_API_URL + 'api/particles';
  public operationUrl = SERVER_API_URL + 'api/operation/particle';

  constructor(protected http: HttpClient) {
  }

  create(particle: IParticle): Observable<EntityResponseType> {
    return this.http.post<IParticle>(this.resourceUrl, particle, {observe: 'response'});
  }

  update(particle: IParticle): Observable<EntityResponseType> {
    return this.http.put<IParticle>(this.resourceUrl, particle, {observe: 'response'});
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParticle>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParticle[]>(this.resourceUrl, {params: options, observe: 'response'});
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  ctrl(opCtrlReq: IOpCtrlReq): Observable<HttpResponse<any>> {
    return this.http.put<IOpCtrlReq>(this.operationUrl, opCtrlReq, {observe: 'response'});
  }

  view(id: number): Observable<EntityOpResponseType> {
    return this.http
    .get<IParticleOp>(`${this.operationUrl}/${id}`, {observe: 'response'})
    .pipe(map((res: EntityOpResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromServer(res: EntityOpResponseType): EntityOpResponseType {
    if (res.body) {
      res.body.dt = res.body.dt != null ? moment(res.body.dt) : null;
      res.body.writtenDt = res.body.writtenDt != null ? moment(res.body.writtenDt) : null;
    }
    return res;
  }
}
