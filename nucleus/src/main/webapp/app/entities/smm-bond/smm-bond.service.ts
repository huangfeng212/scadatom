import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmmBond } from 'app/shared/model/smm-bond.model';

type EntityResponseType = HttpResponse<ISmmBond>;
type EntityArrayResponseType = HttpResponse<ISmmBond[]>;

@Injectable({ providedIn: 'root' })
export class SmmBondService {
    public resourceUrl = SERVER_API_URL + 'api/smm-bonds';

    constructor(protected http: HttpClient) {}

    create(smmBond: ISmmBond): Observable<EntityResponseType> {
        return this.http.post<ISmmBond>(this.resourceUrl, smmBond, { observe: 'response' });
    }

    update(smmBond: ISmmBond): Observable<EntityResponseType> {
        return this.http.put<ISmmBond>(this.resourceUrl, smmBond, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmmBond>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmmBond[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
