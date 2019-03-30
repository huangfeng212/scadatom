import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IElectron} from 'app/shared/model/electron.model';
import {IOpCtrlReq} from 'app/shared/model/operation.model';
import {IElectronOp} from 'app/shared/model/electron-op.model';

type EntityResponseType = HttpResponse<IElectron>;
type EntityArrayResponseType = HttpResponse<IElectron[]>;
type EntityOpResponseType = HttpResponse<IElectronOp>;

@Injectable({providedIn: 'root'})
export class ElectronService {
  public resourceUrl = SERVER_API_URL + 'api/electrons';
  public operationUrl = SERVER_API_URL + 'api/operation/electron';

  constructor(protected http: HttpClient) {
  }

  create(electron: IElectron): Observable<EntityResponseType> {
    return this.http.post<IElectron>(this.resourceUrl, electron, {observe: 'response'});
  }

  update(electron: IElectron): Observable<EntityResponseType> {
    return this.http.put<IElectron>(this.resourceUrl, electron, {observe: 'response'});
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IElectron>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IElectron[]>(this.resourceUrl, {params: options, observe: 'response'});
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }

  ctrl(opCtrlReq: IOpCtrlReq): Observable<HttpResponse<any>> {
    return this.http.put<IOpCtrlReq>(this.operationUrl, opCtrlReq, {observe: 'response'});
  }

  view(id: number): Observable<EntityOpResponseType> {
    return this.http
    .get<IElectronOp>(`${this.operationUrl}/${id}`, {observe: 'response'})
    .pipe(map((res: EntityOpResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromServer(res: EntityOpResponseType): EntityOpResponseType {
    if (res.body) {
      res.body.dt = res.body.dt != null ? moment(res.body.dt) : null;
    }
    return res;
  }
}
