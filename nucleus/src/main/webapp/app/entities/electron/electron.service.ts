import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IElectron } from 'app/shared/model/electron.model';

type EntityResponseType = HttpResponse<IElectron>;
type EntityArrayResponseType = HttpResponse<IElectron[]>;

@Injectable({ providedIn: 'root' })
export class ElectronService {
    public resourceUrl = SERVER_API_URL + 'api/electrons';

    constructor(protected http: HttpClient) {}

    create(electron: IElectron): Observable<EntityResponseType> {
        return this.http.post<IElectron>(this.resourceUrl, electron, { observe: 'response' });
    }

    update(electron: IElectron): Observable<EntityResponseType> {
        return this.http.put<IElectron>(this.resourceUrl, electron, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IElectron>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IElectron[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
