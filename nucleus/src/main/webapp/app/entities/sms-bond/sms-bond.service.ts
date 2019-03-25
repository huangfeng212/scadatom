import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISmsBond } from 'app/shared/model/sms-bond.model';

type EntityResponseType = HttpResponse<ISmsBond>;
type EntityArrayResponseType = HttpResponse<ISmsBond[]>;

@Injectable({ providedIn: 'root' })
export class SmsBondService {
    public resourceUrl = SERVER_API_URL + 'api/sms-bonds';

    constructor(protected http: HttpClient) {}

    create(smsBond: ISmsBond): Observable<EntityResponseType> {
        return this.http.post<ISmsBond>(this.resourceUrl, smsBond, { observe: 'response' });
    }

    update(smsBond: ISmsBond): Observable<EntityResponseType> {
        return this.http.put<ISmsBond>(this.resourceUrl, smsBond, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISmsBond>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISmsBond[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
